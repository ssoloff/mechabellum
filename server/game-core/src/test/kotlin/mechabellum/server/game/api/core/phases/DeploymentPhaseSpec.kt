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

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.jetbrains.spek.subject.SubjectSpek
import org.jetbrains.spek.subject.dsl.SubjectProviderDsl
import kotlin.properties.Delegates

abstract class DeploymentPhaseSpec(
    subjectSpec: SubjectProviderDsl<DeploymentPhase>.() -> Unit
) : SubjectSpek<DeploymentPhase>(subjectSpec) {
    companion object {
        val attackerDeploymentPositions = Position(1, 1)..Position(6, 2)
        val defenderDeploymentPositions = Position(0, 8)..Position(7, 9)

        fun newStrategy(newStrategy: (GridSpecification) -> Strategy): Strategy = newStrategy(
            newTestGridSpecification().copy(
                deploymentPositionsByTeam = mapOf(
                    Team.ATTACKER to attackerDeploymentPositions,
                    Team.DEFENDER to defenderDeploymentPositions
                ),
                type = newTestGridType().copy(cols = 8, rows = 10)
            )
        )
    }

    interface Strategy {
        val game: Game

        fun deployMech(mech: Mech, position: Position)

        fun getMechPosition(mechId: MechId): Position

        fun newMech(mechSpecification: MechSpecification): Mech
    }
}

abstract class CommonDeploymentPhaseSpec(
    newStrategy: (GridSpecification) -> Strategy,
    newSubject: (Strategy, Team) -> DeploymentPhase
) : DeploymentPhaseSpec({
    var strategy: Strategy by Delegates.notNull()
    val deploymentPositions = DeploymentPhaseSpec.defenderDeploymentPositions
    val team = Team.DEFENDER

    subject { newSubject(strategy, team) }

    beforeEachTest {
        strategy = DeploymentPhaseSpec.newStrategy(newStrategy)
    }

    describe("deployMech") {
        it("should place Mech at specified position") {
            // given
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))

            // when
            val position = Position(3, 2)
            subject.deployMech(mech, position)

            // then
            strategy.getMechPosition(mech.id) shouldEqual position
        }

        it("should throw exception when Mech does not exist") {
            // given
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
            }

            // when
            val operation = { subject.deployMech(mech, Position(3, 2)) }

            // then
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }

        it("should throw exception when Mech belongs to a different team") {
            // given
            val mech = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when
            val operation = { subject.deployMech(mech, Position(3, 2)) }

            // then
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "team"
        }

        on(
            "invalid deployment position %s",
            data(
                deploymentPositions.start.copy(col = deploymentPositions.start.col - 1),
                expected = Unit
            ),
            data(
                deploymentPositions.start.copy(row = deploymentPositions.start.row - 1),
                expected = Unit
            ),
            data(
                deploymentPositions.endInclusive.copy(col = deploymentPositions.endInclusive.col + 1),
                expected = Unit
            ),
            data(
                deploymentPositions.endInclusive.copy(row = deploymentPositions.endInclusive.row + 1),
                expected = Unit
            )
        ) { position, _ ->
            it("should throw exception") {
                // given
                val mech = strategy.newMech(newTestMechSpecification().copy(team = team))

                // when
                val operation = { subject.deployMech(mech, position) }

                // then
                val exceptionResult = operation shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "position"
            }
        }
    }
})

abstract class AttackerDeploymentPhaseSpec(
    newStrategy: (GridSpecification) -> Strategy,
    newSubject: (Strategy, Team) -> DeploymentPhase
) : DeploymentPhaseSpec({
    var strategy: Strategy by Delegates.notNull()

    subject { newSubject(strategy, Team.ATTACKER) }

    beforeEachTest {
        strategy = DeploymentPhaseSpec.newStrategy(newStrategy)
    }

    describe("end") {
        it("should change active phase to initiative phase") {
            // given: all attackers have been deployed
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            strategy.deployMech(attacker, DeploymentPhaseSpec.attackerDeploymentPositions.start)

            // when: attacker deployment phase is ended
            subject.end()

            // then: initiative phase should be active
            strategy.game.phase shouldBeInstanceOf InitiativePhase::class
        }

        it("should throw exception when all attackers have not been deployed") {
            // given: all attackers have not been deployed
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when: attacker deployment phase is ended
            val operation = { subject.end() }

            // then: an exception should be thrown
            operation
                .shouldThrow(GameException::class)
                .withMessage("Mech with ID ${attacker.id} has not been deployed")
        }
    }
})

abstract class DefenderDeploymentPhaseSpec(
    newStrategy: (GridSpecification) -> Strategy,
    newSubject: (Strategy, Team) -> DeploymentPhase
) : DeploymentPhaseSpec({
    var strategy: Strategy by Delegates.notNull()

    subject { newSubject(strategy, Team.DEFENDER) }

    beforeEachTest {
        strategy = DeploymentPhaseSpec.newStrategy(newStrategy)
    }

    describe("end") {
        it("should change active phase to initiative phase") {
            // given: all defenders have been deployed
            val defender = strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            strategy.deployMech(defender, DeploymentPhaseSpec.defenderDeploymentPositions.start)

            // when: defender deployment phase is ended
            subject.end()

            // then: attacker deployment phase should be active
            strategy.game.phase shouldBeInstanceOf DeploymentPhase::class
            (strategy.game.phase as DeploymentPhase).team shouldEqual Team.ATTACKER
        }

        it("should throw exception when all defenders have not been deployed") {
            // given: all defenders have not been deployed
            val defender = strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))

            // when: defender deployment phase is ended
            val operation = { subject.end() }

            // then: an exception should be thrown
            operation
                .shouldThrow(GameException::class)
                .withMessage("Mech with ID ${defender.id} has not been deployed")
        }
    }
})
