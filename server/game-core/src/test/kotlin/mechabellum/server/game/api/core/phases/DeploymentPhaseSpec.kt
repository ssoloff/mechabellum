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

abstract class DeploymentPhaseSpec(
    getMechPosition: DeploymentPhase.(MechId) -> Position,
    newMech: DeploymentPhase.(MechSpecification) -> Mech,
    subjectFactory: (GridSpecification, Team) -> DeploymentPhase
) : SubjectSpek<DeploymentPhase>({
    val gridType = newTestGridType().copy(cols = 8, rows = 10)
    val attackerDeploymentPositions = Position(1, 1)..Position(6, 2)
    val defenderDeploymentPositions = Position(0, 8)..Position(7, 9)

    subject {
        subjectFactory(
            newTestGridSpecification().copy(
                deploymentPositionsByTeam = mapOf(
                    Team.ATTACKER to attackerDeploymentPositions,
                    Team.DEFENDER to defenderDeploymentPositions
                ),
                type = gridType
            ),
            Team.DEFENDER
        )
    }

    describe("deployMech") {
        it("should place Mech at specified position") {
            // given
            val mech = subject.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))

            // when
            val position = Position(3, 2)
            subject.deployMech(mech, position)

            // then
            subject.getMechPosition(mech.id) shouldEqual position
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
            val mech = subject.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when
            val operation = { subject.deployMech(mech, Position(3, 2)) }

            // then
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "team"
        }

        on(
            "invalid deployment position %s",
            data(
                defenderDeploymentPositions.start.copy(col = defenderDeploymentPositions.start.col - 1),
                expected = Unit
            ),
            data(
                defenderDeploymentPositions.start.copy(row = defenderDeploymentPositions.start.row - 1),
                expected = Unit
            ),
            data(
                defenderDeploymentPositions.endInclusive.copy(col = defenderDeploymentPositions.endInclusive.col + 1),
                expected = Unit
            ),
            data(
                defenderDeploymentPositions.endInclusive.copy(row = defenderDeploymentPositions.endInclusive.row + 1),
                expected = Unit
            )
        ) { position, _ ->
            it("should throw exception") {
                // given
                val mech = subject.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))

                // when
                val operation = { subject.deployMech(mech, position) }

                // then
                val exceptionResult = operation shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "position"
            }
        }
    }

    describe("end") {
        describe("when defender deployment phase is active") {
            it("should change active phase to initiative phase") {
                // given: each team has one Mech
                val initializationPhase = subject.game.phase as InitializationPhase
                initializationPhase.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
                val defender = initializationPhase.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
                initializationPhase.end()
                // and: all defender Mechs have been deployed
                val defenderDeploymentPhase = subject.game.phase as DeploymentPhase
                defenderDeploymentPhase.deployMech(defender, defenderDeploymentPositions.start)

                // when: defender deployment phase is ended
                defenderDeploymentPhase.end()

                // then: attacker deployment phase should be active
                subject.game.phase shouldBeInstanceOf DeploymentPhase::class
                (subject.game.phase as DeploymentPhase).team shouldEqual Team.ATTACKER
            }

            it("should throw exception when all team Mechs have not been deployed") {
                // given: each team has one Mech
                val initializationPhase = subject.game.phase as InitializationPhase
                initializationPhase.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
                val defender = initializationPhase.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
                initializationPhase.end()
                // but: all defenders have not been deployed
                val defenderDeploymentPhase = subject.game.phase as DeploymentPhase

                // when: defender deployment phase is ended
                val operation = { defenderDeploymentPhase.end() }

                // then: an exception should be thrown
                operation
                    .shouldThrow(GameException::class)
                    .withMessage("Mech with ID ${defender.id} has not been deployed")
            }
        }

        describe("when attacker deployment phase is active") {
            it("should change active phase to initiative phase") {
                // given: each team has one Mech
                val initializationPhase = subject.game.phase as InitializationPhase
                val attacker = initializationPhase.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
                val defender = initializationPhase.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
                initializationPhase.end()
                // and: all defenders have been deployed
                val defenderDeploymentPhase = subject.game.phase as DeploymentPhase
                defenderDeploymentPhase.deployMech(defender, defenderDeploymentPositions.start)
                defenderDeploymentPhase.end()
                // and: all attackers have been deployed
                val attackerDeploymentPhase = subject.game.phase as DeploymentPhase
                attackerDeploymentPhase.deployMech(attacker, attackerDeploymentPositions.start)

                // when: attacker deployment phase is ended
                attackerDeploymentPhase.end()

                // then: initiative phase should be active
                subject.game.phase shouldBeInstanceOf InitiativePhase::class
            }

            it("should throw exception when all team Mechs have not been deployed") {
                // given: each team has one Mech
                val initializationPhase = subject.game.phase as InitializationPhase
                val attacker = initializationPhase.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
                val defender = initializationPhase.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
                initializationPhase.end()
                // and: all defenders have been deployed
                val defenderDeploymentPhase = subject.game.phase as DeploymentPhase
                defenderDeploymentPhase.deployMech(defender, defenderDeploymentPositions.start)
                defenderDeploymentPhase.end()
                // but: all attackers have not been deployed
                val attackerDeploymentPhase = subject.game.phase as DeploymentPhase

                // when: attacker deployment phase is ended
                val operation = { attackerDeploymentPhase.end() }

                // then: an exception should be thrown
                operation
                    .shouldThrow(GameException::class)
                    .withMessage("Mech with ID ${attacker.id} has not been deployed")
            }
        }
    }
})
