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
import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.newTestGameSpecification
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

        fun newStrategy(newStrategy: (GameSpecification) -> Strategy): Strategy = newStrategy(
            newTestGameSpecification().copy(
                gridSpecification = newTestGridSpecification().copy(
                    deploymentPositionsByTeam = mapOf(
                        Team.ATTACKER to attackerDeploymentPositions,
                        Team.DEFENDER to defenderDeploymentPositions
                    ),
                    type = newTestGridType().copy(cols = 8, rows = 10)
                )
            )
        )
    }

    interface Strategy {
        val game: Game

        fun deploy(mech: Mech, position: Position, facing: Direction)

        fun getMech(mechId: MechId): Mech

        fun newMech(mechSpecification: MechSpecification): Mech
    }
}

abstract class CommonDeploymentPhaseSpec(
    newStrategy: (GameSpecification) -> Strategy,
    newSubject: (Strategy, Team) -> DeploymentPhase
) : DeploymentPhaseSpec({
    var strategy: Strategy by Delegates.notNull()
    val deploymentPositions = DeploymentPhaseSpec.defenderDeploymentPositions
    val team = Team.DEFENDER

    subject { newSubject(strategy, team) }

    beforeEachTest {
        strategy = DeploymentPhaseSpec.newStrategy(newStrategy)
    }

    describe("deploy") {
        it("should deploy Mech with specified position and facing") {
            // given: a Mech associated with the deploying team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))

            // when: deploying the Mech
            val position = Position(3, 2)
            val facing = Direction.NORTH
            subject.deploy(mech, position, facing)

            // then: it should be deployed with the specified position and facing
            strategy.getMech(mech.id).position shouldEqual Option.some(position)
            strategy.getMech(mech.id).facing shouldEqual Option.some(facing)
        }

        it("should throw exception when Mech does not exist") {
            // given: a Mech that was not created by the game
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
                on { this.team } doReturn team
            }

            // when: deploying the Mech
            val operation = { subject.deploy(mech, Position(3, 2), Direction.NORTH) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }

        it("should throw exception when Mech belongs to a different team") {
            // given: a Mech not associated with the deploying team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when: deploying the Mech
            val operation = { subject.deploy(mech, Position(3, 2), Direction.NORTH) }

            // then: it should throw an exception
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
                // given: a Mech associated with the deploying team
                val mech = strategy.newMech(newTestMechSpecification().copy(team = team))

                // when: deploying the Mech to an invalid position
                val operation = { subject.deploy(mech, position, Direction.NORTH) }

                // then: it should throw an exception
                val exceptionResult = operation shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "position"
            }
        }
    }
})

abstract class AttackerDeploymentPhaseSpec(
    newStrategy: (GameSpecification) -> Strategy,
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
            strategy.deploy(attacker, DeploymentPhaseSpec.attackerDeploymentPositions.start, Direction.NORTH)

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
                .shouldThrow(IllegalStateException::class)
                .withMessage("Mechs ${listOf(attacker.id)} from team ${Team.ATTACKER} have not been deployed")
        }
    }
})

abstract class DefenderDeploymentPhaseSpec(
    newStrategy: (GameSpecification) -> Strategy,
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
            strategy.deploy(defender, DeploymentPhaseSpec.defenderDeploymentPositions.start, Direction.NORTH)

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
                .shouldThrow(IllegalStateException::class)
                .withMessage("Mechs ${listOf(defender.id)} from team ${Team.DEFENDER} have not been deployed")
        }
    }
})
