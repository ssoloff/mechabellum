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
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.jetbrains.spek.subject.SubjectSpek

abstract class DeploymentPhaseSpec(
    getMechPosition: DeploymentPhase.(MechId) -> Position,
    newMech: DeploymentPhase.(MechSpecification) -> Mech,
    subjectFactory: (GridSpecification) -> DeploymentPhase
) : SubjectSpek<DeploymentPhase>({
    val gridType = newTestGridType().copy(cols = 8, rows = 10)
    val attackerDeploymentZone = Position(1, 1)..Position(6, 2)

    subject {
        subjectFactory(
            newTestGridSpecification().copy(
                deploymentZonesByTeam = mapOf(
                    Team.ATTACKER to attackerDeploymentZone,
                    Team.DEFENDER to Position(0, 8)..Position(7, 9)
                ),
                type = gridType
            )
        )
    }

    describe("deployMech") {
        it("should place Mech at specified position") {
            // given
            val mech = subject.newMech(newTestMechSpecification())

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

        on(
            "invalid attacker position %s",
            data(
                attackerDeploymentZone.start.copy(col = attackerDeploymentZone.start.col - 1),
                expected = Unit
            ),
            data(
                attackerDeploymentZone.start.copy(row = attackerDeploymentZone.start.row - 1),
                expected = Unit
            ),
            data(
                attackerDeploymentZone.endInclusive.copy(col = attackerDeploymentZone.endInclusive.col + 1),
                expected = Unit
            ),
            data(
                attackerDeploymentZone.endInclusive.copy(row = attackerDeploymentZone.endInclusive.row + 1),
                expected = Unit
            )
        ) { position, _ ->
            it("should throw exception") {
                // given
                val mech = subject.newMech(newTestMechSpecification())

                // when
                val operation = { subject.deployMech(mech, position) }

                // then
                val exceptionResult = operation shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "position"
            }
        }
    }
})
