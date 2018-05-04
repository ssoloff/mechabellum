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

package mechabellum.server.game.api.core.grid

import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class GridSpec(subjectFactory: (GridSpecification) -> Grid) : SubjectSpek<Grid>({
    val gridSpecification = newTestGridSpecification().copy(type = newTestGridType().copy(cols = 5, rows = 8))

    subject { subjectFactory(gridSpecification) }

    describe("type") {
        it("should return type from grid specification") {
            subject.type shouldEqual gridSpecification.type
        }
    }

    describe("getCell") {
        it("should return requested cell when cell exists") {
            subject.getCell(0, 0).id shouldEqual CellId(0, 0)
            subject.getCell(4, 7).id shouldEqual CellId(4, 7)
        }

        it("should throw exception when cell does not exist") {
            ({ subject.getCell(-1, 0) }) shouldThrow IllegalArgumentException::class
            ({ subject.getCell(0, -1) }) shouldThrow IllegalArgumentException::class
            ({ subject.getCell(5, 7) }) shouldThrow IllegalArgumentException::class
            ({ subject.getCell(4, 8) }) shouldThrow IllegalArgumentException::class
        }
    }

    describe("getDeploymentZone") {
        it("should return deployment zones from grid specification") {
            Team.values().forEach {
                subject.getDeploymentZone(it) shouldEqual gridSpecification.deploymentZonesByTeam[it]
            }
        }
    }
})
