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
import mechabellum.server.common.api.test.ranges.ClosedRangeSpec
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object PositionSpec : Spek({
    describe("rangeTo") {
        it("should return a position range with an inclusive end") {
            val start = Position(1, 2)
            val endInclusive = Position(11, 22)
            start..endInclusive shouldEqual PositionRange(start, endInclusive)
        }
    }

    describe("until") {
        it("should return a position range with an exclusive end") {
            val start = Position(1, 2)
            val endInclusive = Position(11, 22)
            val endExclusive = Position(endInclusive.col - 1, endInclusive.row - 1)
            start until endInclusive shouldEqual PositionRange(start, endExclusive)
        }
    }
})

object PositionBehavesAsComparableSpec : ComparableSpec<Position>(
    newGreaterThanInstances = { listOf(Position(12, 22), Position(11, 23)) },
    newLessThanInstances = { listOf(Position(10, 22), Position(11, 21)) },
    newReferenceInstance = { Position(11, 22) }
)

object PositionBehavesAsDataClassSpec : DataClassSpec({ Position(11, 22) })

object PositionRangeBehavesAsClosedRangeSpec : ClosedRangeSpec<Position>(
    inRangeValue = Position(5, 5),
    newEmptyInstance = { PositionRange(Position(0, 0), Position(-1, -1)) },
    newNonEmptyInstance = { PositionRange(Position(0, 0), Position(10, 10)) },
    outOfRangeValue = Position(11, 11)
)

object PositionRangeBehavesAsDataClassSpec : DataClassSpec({ PositionRange(Position(0, 0), Position(5, 8)) })
