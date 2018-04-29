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

package mechabellum.server.game.internal.core

import mechabellum.server.game.api.core.CommandContextSpec
import mechabellum.server.game.api.core.GameSpec
import mechabellum.server.game.api.core.features.DeploymentFeature
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import mechabellum.server.game.internal.core.grid.InternalGrid
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

object InternalGameBehavesAsCommandContextSpec : CommandContextSpec(
    presentFeatureType = DeploymentFeature::class.java,
    subjectFactory = { InternalGame(InternalGrid(newTestGridType())) }
)

internal object InternalGameBehavesAsDeploymentFeatureSpec : SubjectSpek<InternalGame>({
    subject { InternalGame(InternalGrid(newTestGridType(8, 10))) }

    describe("deployMech") {
        it("should add Mech to game") {
            // when
            val mech = subject.deployMech(newTestMechSpecification(), CellId(3, 6))

            // then
            subject.getMech(mech.id) shouldBe mech
        }

        it("should place Mech at specified position") {
            // when
            val position = CellId(3, 6)
            val mech = subject.deployMech(newTestMechSpecification(), position)

            // then
            subject.getMechPosition(mech.id) shouldEqual position
        }

        it("should throw exception when position is invalid") {
            // when
            val grid = subject.grid
            val func = { subject.deployMech(newTestMechSpecification(), CellId(grid.type.cols, grid.type.rows)) }

            // then
            val exceptionResult = func shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "position"
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

object InternalGameBehavesAsGameSpec : GameSpec({ InternalGame(InternalGrid(newTestGridType())) })

internal object InternalGameBehavesAsGridFeatureSpec : SubjectSpek<InternalGame>({
    val grid = InternalGrid(newTestGridType())

    subject { InternalGame(grid) }

    describe("grid") {
        it("should return game grid") {
            subject.grid shouldBe grid
        }
    }
})
