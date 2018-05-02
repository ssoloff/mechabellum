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
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.grid.newTestGridType
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
    getMechPosition: (DeploymentPhase, MechId) -> CellId,
    newMech: (DeploymentPhase, MechSpecification) -> Mech,
    subjectFactory: (GridSpecification) -> DeploymentPhase
) : SubjectSpek<DeploymentPhase>({
    val gridType = newTestGridType(8, 10)

    subject { subjectFactory(newTestGridSpecification(gridType)) }

    describe("deployMech") {
        it("should place Mech at specified position") {
            // given
            val mech = newMech(subject, newTestMechSpecification())

            // when
            val position = CellId(3, 6)
            subject.deployMech(mech, position)

            // then
            getMechPosition(subject, mech.id) shouldEqual position
        }

        it("should throw exception when Mech does not exist") {
            // given
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
            }

            // when
            val func = { subject.deployMech(mech, CellId(3, 6)) }

            // then
            val exceptionResult = func shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }

        on(
            "invalid position %s",
            data(CellId(-1, 0), expected = Unit),
            data(CellId(0, -1), expected = Unit),
            data(CellId(gridType.cols, 0), expected = Unit),
            data(CellId(0, gridType.rows), expected = Unit)
        ) { position, _ ->
            it("should throw exception") {
                // given
                val mech = newMech(subject, newTestMechSpecification())

                // when
                val func = { subject.deployMech(mech, position) }

                // then
                val exceptionResult = func shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "position"
            }
        }
    }
})
