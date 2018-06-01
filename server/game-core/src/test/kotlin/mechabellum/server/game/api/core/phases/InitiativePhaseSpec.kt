/*
 * Copyright (C) 2018 Mechabellum contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mechabellum.server.game.api.core.phases

import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.ScriptedDieRoller
import mechabellum.server.game.api.core.newTestGameSpecification
import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.properties.Delegates

abstract class InitiativePhaseSpec(
    newStrategy: (GameSpecification) -> Strategy,
    newSubject: (Strategy) -> InitiativePhase
) : Spek({
    var scriptedDieRoller: ScriptedDieRoller by Delegates.notNull()
    var strategy: Strategy by Delegates.notNull()

    beforeEachTest {
        scriptedDieRoller = ScriptedDieRoller()
        strategy = newStrategy(newTestGameSpecification().copy(dieRoller = scriptedDieRoller))
    }

    describe("constructor") {
        it("should roll initiative for each team") {
            // given: a die roller that produces a sequence that will result in unique initiative values
            scriptedDieRoller.addValues(1, 2, 5, 6)

            // when: initiative phase is created
            newSubject(strategy)

            // then: it should roll initiative for each team in lexicographic order
            strategy.game.turn.getInitiativeRoll(Team.ATTACKER) shouldEqual 3
            strategy.game.turn.getInitiativeRoll(Team.DEFENDER) shouldEqual 11
        }

        it("should re-roll initiative for each team in the event of a tie") {
            // given: a die roller that produces a sequence that will result in identical initiative values
            scriptedDieRoller.addValues(1, 3, 3, 1)
            // and: followed by a sequence that will result in unique initiative values
            scriptedDieRoller.addValues(1, 2, 5, 6)

            // when: initiative phase is created
            newSubject(strategy)

            // then: it should roll initiative for each team twice to resolve the tie
            strategy.game.turn.getInitiativeRoll(Team.ATTACKER) shouldEqual 3
            strategy.game.turn.getInitiativeRoll(Team.DEFENDER) shouldEqual 11
        }
    }

    describe("end") {
        it("should change active phase to attacker movement phase when attacker has initiative") {
            // given: the attacker has initiative
            scriptedDieRoller.addValues(6, 6, 1, 1)
            val subject = newSubject(strategy)

            // when: initiative phase is ended
            subject.end()

            // then: attacker movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.ATTACKER
        }

        it("should change active phase to defender movement phase when defender has initiative") {
            // given: the defender has initiative
            scriptedDieRoller.addValues(1, 1, 6, 6)
            val subject = newSubject(strategy)

            // when: initiative phase is ended
            subject.end()

            // then: defender movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.DEFENDER
        }
    }
}) {
    interface Strategy {
        val game: Game
    }
}
