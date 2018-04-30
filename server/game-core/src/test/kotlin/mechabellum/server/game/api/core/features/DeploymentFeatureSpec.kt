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

package mechabellum.server.game.api.core.features

import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class DeploymentFeatureSpec(
    getMech: (DeploymentFeature, MechId) -> Mech,
    getMechPosition: (DeploymentFeature, MechId) -> CellId,
    subjectFactory: (GridSpecification) -> DeploymentFeature
) : SubjectSpek<DeploymentFeature>({
    val gridType = newTestGridType(8, 10)

    subject { subjectFactory(newTestGridSpecification(gridType)) }

    describe("deployMech") {
        it("should add Mech to game") {
            // when
            val mech = subject.deployMech(newTestMechSpecification(), CellId(3, 6))

            // then
            getMech(subject, mech.id) shouldBe mech
        }

        it("should place Mech at specified position") {
            // when
            val position = CellId(3, 6)
            val mech = subject.deployMech(newTestMechSpecification(), position)

            // then
            getMechPosition(subject, mech.id) shouldEqual position
        }

        listOf(
            CellId(-1, 0),
            CellId(0, -1),
            CellId(gridType.cols, 0),
            CellId(0, gridType.rows)
        ).forEach {
            it("should throw exception when position is invalid ($it)") {
                // when
                val func = { subject.deployMech(newTestMechSpecification(), it) }

                // then
                val exceptionResult = func shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "position"
            }
        }

        it("should create Mechs with distinct identifiers") {
            // when
            val mech1 = subject.deployMech(newTestMechSpecification(), CellId(3, 6))
            val mech2 = subject.deployMech(newTestMechSpecification(), CellId(4, 6))

            // then
            mech1.id shouldNotEqual mech2.id
        }
    }
})
