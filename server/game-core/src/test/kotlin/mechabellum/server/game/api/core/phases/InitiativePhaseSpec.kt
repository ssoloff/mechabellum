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
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.mechanics.ScriptedDieRoller
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

    describe("end") {
        it("should change active phase to defender movement phase when attacker won initiative") {
            // given: the attacker won initiative
            dieRoller.addValues(6, 6, 1, 1)
            subject.rollInitiative(Team.ATTACKER)
            subject.rollInitiative(Team.DEFENDER)

            // when: initiative phase is ended
            subject.end()

            // then: defender movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.DEFENDER
        }

        it("should change active phase to attacker movement phase when defender won initiative") {
            // given: the defender won initiative
            dieRoller.addValues(1, 1, 6, 6)
            subject.rollInitiative(Team.ATTACKER)
            subject.rollInitiative(Team.DEFENDER)

            // when: initiative phase is ended
            subject.end()

            // then: attacker movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.ATTACKER
        }

        it("should throw exception when no team has rolled initiative") {
            // given: no team has rolled initiative

            // when: initiative phase is ended
            val operation = { subject.end() }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "team(s) [${Team.ATTACKER}, ${Team.DEFENDER}] have not rolled initiative"
        }

        it("should throw exception when one team has not rolled initiative") {
            // given: attacker has rolled initiative
            dieRoller.addValues(1, 1)
            subject.rollInitiative(Team.ATTACKER)
            // but: defender has not rolled initiative

            // when: initiative phase is ended
            val operation = { subject.end() }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "team(s) [${Team.DEFENDER}] have not rolled initiative"
        }

        it("should throw exception when all teams have rolled initiative but there is no winner") {
            // given: attacker and defender have rolled same initiative result
            dieRoller.addValues(1, 1, 1, 1)
            subject.rollInitiative(Team.ATTACKER)
            subject.rollInitiative(Team.DEFENDER)

            // when: initiative phase is ended
            val operation = { subject.end() }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "all teams rolled initiative but there is no winner; each team must re-roll"
        }
    }

    describe("rollInitiative") {
        it("should roll initiative for specified team") {
            // given: a die roller that produces an initiative result sequence of (11)
            dieRoller.addValues(5, 6)

            // when: initiative is rolled for attacker
            val attackerInitiative = subject.rollInitiative(Team.ATTACKER)

            // then: it should roll initiative 11 for attacker
            attackerInitiative shouldEqual Initiative(11)
        }

        it("should allow rolling for initiative for all teams at least once") {
            // given: a die roller that produces an initiative result sequence of (11, 3)
            dieRoller.addValues(5, 6, 1, 2)

            // when: initiative is rolled for attacker
            val attackerInitiative = subject.rollInitiative(Team.ATTACKER)
            // and: initiative is rolled for defender
            val defenderInitiative = subject.rollInitiative(Team.DEFENDER)

            // then: it should roll initiative 11 for attacker
            attackerInitiative shouldEqual Initiative(11)
            // and: it should roll initiative 3 for defender
            defenderInitiative shouldEqual Initiative(3)
        }

        it("should throw exception when re-rolling team initiative in the same iteration") {
            // given: a die roller that produces an initiative result sequence of (11, 3)
            dieRoller.addValues(5, 6, 1, 2)

            // when: initiative is rolled for attacker
            subject.rollInitiative(Team.ATTACKER)
            // and: initiative is re-rolled for attacker
            val operation = { subject.rollInitiative(Team.ATTACKER) }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "illegal attempt to re-roll initiative for team ${Team.ATTACKER}"
        }

        it("should allow another iteration of rolling for initiative if no winner in most recent iteration") {
            // given: a die roller that produces an initiative result sequence of (11, 11, 7, 3)
            dieRoller.addValues(5, 6, 5, 6, 3, 4, 1, 2)

            // when: initiative is rolled for attacker and defender
            subject.rollInitiative(Team.ATTACKER)
            subject.rollInitiative(Team.DEFENDER)
            // and: initiative is re-rolled for attacker and defender
            val attackerInitiative = subject.rollInitiative(Team.ATTACKER)
            val defenderInitiative = subject.rollInitiative(Team.DEFENDER)

            // then: it should roll initiative 7 for attacker
            attackerInitiative shouldEqual Initiative(7)
            // and: it should roll initiative 3 for defender
            defenderInitiative shouldEqual Initiative(3)
        }
    }
}) {
    interface Strategy {
        val game: Game
    }
}
