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
import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import kotlin.properties.Delegates

abstract class InitializationPhaseSpec(
    newStrategy: (GridSpecification) -> Strategy,
    newSubject: (Strategy) -> InitializationPhase
) : SubjectSpek<InitializationPhase>({
    var strategy: Strategy by Delegates.notNull()

    subject { newSubject(strategy) }

    beforeEachTest {
        strategy = newStrategy(newTestGridSpecification())
    }

    describe("end") {
        it("should change active phase to defender deployment phase") {
            // given: all teams have at least one Mech
            strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))

            // when: initialization phase is ended
            subject.end()

            // then: defender deployment phase should be active
            strategy.game.phase shouldBeInstanceOf DeploymentPhase::class
            (strategy.game.phase as DeploymentPhase).team shouldEqual Team.DEFENDER
        }

        it("should throw exception when attacker has no Mechs") {
            // given: defender has at least one Mech
            strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // but: attacker has no Mechs

            // when: initialization phase is ended
            val operation = { subject.end() }

            // then: an exception should be thrown
            operation shouldThrow GameException::class withMessage "attacker has no Mechs"
        }

        it("should throw exception when defender has no Mechs") {
            // given: attacker has at least one Mech
            strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            // but: defender has no Mechs

            // when: initialization phase is ended
            val operation = { subject.end() }

            // then: an exception should be thrown
            operation shouldThrow GameException::class withMessage "defender has no Mechs"
        }
    }

    describe("newMech") {
        it("should add Mech to game") {
            // when: new Mech is created
            val mech = subject.newMech(newTestMechSpecification())

            // then: it should be added to the game
            strategy.getMech(mech.id) shouldBe mech
        }

        it("should create Mechs with distinct identifiers") {
            // when: multiple Mechs are created
            val mech1 = subject.newMech(newTestMechSpecification())
            val mech2 = subject.newMech(newTestMechSpecification())

            // then: each mech should have a distinct identifier
            mech1.id shouldNotEqual mech2.id
        }
    }
}) {
    interface Strategy {
        val game: Game

        fun getMech(mechId: MechId): Mech

        fun newMech(mechSpecification: MechSpecification): Mech
    }
}
