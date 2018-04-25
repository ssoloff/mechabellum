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

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DirectionSpec : Spek({
    it("should define exactly six directions") {
        Direction.values().size shouldEqualTo 6
    }

    describe("clockwise") {
        it("should return direction immediately clockwise") {
            Direction.NORTH.clockwise shouldBe Direction.NORTHEAST
            Direction.NORTHEAST.clockwise shouldBe Direction.SOUTHEAST
            Direction.SOUTHEAST.clockwise shouldBe Direction.SOUTH
            Direction.SOUTH.clockwise shouldBe Direction.SOUTHWEST
            Direction.SOUTHWEST.clockwise shouldBe Direction.NORTHWEST
            Direction.NORTHWEST.clockwise shouldBe Direction.NORTH
        }
    }

    describe("counterclockwise") {
        it("should return direction immediately counterclockwise") {
            Direction.NORTH.counterclockwise shouldBe Direction.NORTHWEST
            Direction.NORTHEAST.counterclockwise shouldBe Direction.NORTH
            Direction.SOUTHEAST.counterclockwise shouldBe Direction.NORTHEAST
            Direction.SOUTH.counterclockwise shouldBe Direction.SOUTHEAST
            Direction.SOUTHWEST.counterclockwise shouldBe Direction.SOUTH
            Direction.NORTHWEST.counterclockwise shouldBe Direction.SOUTHWEST
        }
    }

    describe("opposite") {
        it("should return direction directly opposite") {
            Direction.NORTH.opposite shouldBe Direction.SOUTH
            Direction.NORTHEAST.opposite shouldBe Direction.SOUTHWEST
            Direction.SOUTHEAST.opposite shouldBe Direction.NORTHWEST
            Direction.SOUTH.opposite shouldBe Direction.NORTH
            Direction.SOUTHWEST.opposite shouldBe Direction.NORTHEAST
            Direction.NORTHWEST.opposite shouldBe Direction.SOUTHEAST
        }
    }
})
