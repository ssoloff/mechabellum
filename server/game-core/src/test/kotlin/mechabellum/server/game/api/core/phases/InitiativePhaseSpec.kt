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

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.ScriptedDieRoller
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.newTestGameSpecification
import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import kotlin.properties.Delegates

abstract class InitiativePhaseSpec(
    newStrategy: (GameSpecification) -> Strategy,
    newSubject: (Strategy) -> InitiativePhase
) : SubjectSpek<InitiativePhase>({
    var dieRoller: ScriptedDieRoller by Delegates.notNull()
    var strategy: Strategy by Delegates.notNull()

    subject { newSubject(strategy) }

    beforeEachTest {
        dieRoller = ScriptedDieRoller()
        strategy = newStrategy(newTestGameSpecification().copy(dieRoller = dieRoller))
    }

    describe("rollInitiative") {
        it("should roll initiative for each team") {
            // given: a die roller that produces a sequence that will result in unique initiative values
            dieRoller.addValues(1, 2, 5, 6)

            // when: initiative is rolled
            subject.rollInitiative()

            // then: it should roll initiative for each team in lexicographic order
            strategy.game.turn.getInitiative(Team.ATTACKER) shouldEqual Option.some(Initiative(3))
            strategy.game.turn.getInitiative(Team.DEFENDER) shouldEqual Option.some(Initiative(11))
        }

        it("should re-roll initiative for each team in the event of a tie") {
            // given: a die roller that produces a sequence that will result in identical initiative values
            dieRoller.addValues(1, 3, 3, 1)
            // and: followed by a sequence that will result in unique initiative values
            dieRoller.addValues(1, 2, 5, 6)

            // when: initiative is rolled
            subject.rollInitiative()

            // then: it should roll initiative for each team twice to resolve the tie
            strategy.game.turn.getInitiative(Team.ATTACKER) shouldEqual Option.some(Initiative(3))
            strategy.game.turn.getInitiative(Team.DEFENDER) shouldEqual Option.some(Initiative(11))
        }
    }

    describe("end") {
        it("should change active phase to attacker movement phase when attacker has initiative") {
            // given: the attacker has initiative
            dieRoller.addValues(6, 6, 1, 1)
            subject.rollInitiative()

            // when: initiative phase is ended
            subject.end()

            // then: attacker movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.ATTACKER
        }

        it("should change active phase to defender movement phase when defender has initiative") {
            // given: the defender has initiative
            dieRoller.addValues(1, 1, 6, 6)
            subject.rollInitiative()

            // when: initiative phase is ended
            subject.end()

            // then: defender movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.DEFENDER
        }

        it("should throw exception when initiative has not been rolled") {
            // given: initiative has not been rolled

            // when: initiative phase is ended
            val operation = { subject.end() }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "expected initiative to be rolled but was not"
        }
    }
}) {
    interface Strategy {
        val game: Game
    }
}
