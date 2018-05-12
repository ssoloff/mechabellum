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

import mechabellum.server.common.api.test.ComparableSpec
import mechabellum.server.common.api.test.DataClassSpec
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object CellIdSpec : Spek({
    describe("rangeTo") {
        it("should return a cell range with an inclusive end") {
            val start = CellId(1, 2)
            val endInclusive = CellId(11, 22)
            start..endInclusive shouldEqual CellRange(start, endInclusive)
        }
    }

    describe("until") {
        it("should return a cell range with an exclusive end") {
            val start = CellId(1, 2)
            val endInclusive = CellId(11, 22)
            start until endInclusive shouldEqual CellRange(start, CellId(endInclusive.col - 1, endInclusive.row - 1))
        }
    }
})

object CellIdBehavesAsComparableSpec : ComparableSpec<CellId>(
    newGreaterThanInstances = { listOf(CellId(12, 22), CellId(11, 23)) },
    newLessThanInstances = { listOf(CellId(10, 22), CellId(11, 21)) },
    newReferenceInstance = { CellId(11, 22) }
)

object CellIdBehavesAsDataClassSpec : DataClassSpec({ CellId(11, 22) })
