/*
 * Copyright (C) 2019 Mechabellum contributors
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
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

object DirectionSpec : Spek({
    it("should define one direction per cell side") {
        Direction.values().size shouldEqualTo Cell.SIDE_COUNT
    }

    describe("clockwise") {
        on(
            "%s",
            data(Direction.NORTH, expected = Direction.NORTHEAST),
            data(Direction.NORTHEAST, expected = Direction.SOUTHEAST),
            data(Direction.SOUTHEAST, expected = Direction.SOUTH),
            data(Direction.SOUTH, expected = Direction.SOUTHWEST),
            data(Direction.SOUTHWEST, expected = Direction.NORTHWEST),
            data(Direction.NORTHWEST, expected = Direction.NORTH)
        ) { direction, expected ->
            it("should return $expected") {
                direction.clockwise shouldBe expected
            }
        }
    }

    describe("counterclockwise") {
        on(
            "%s",
            data(Direction.NORTH, expected = Direction.NORTHWEST),
            data(Direction.NORTHEAST, expected = Direction.NORTH),
            data(Direction.SOUTHEAST, expected = Direction.NORTHEAST),
            data(Direction.SOUTH, expected = Direction.SOUTHEAST),
            data(Direction.SOUTHWEST, expected = Direction.SOUTH),
            data(Direction.NORTHWEST, expected = Direction.SOUTHWEST)
        ) { direction, expected ->
            it("should return $expected") {
                direction.counterclockwise shouldBe expected
            }
        }
    }

    describe("opposite") {
        on(
            "%s",
            data(Direction.NORTH, expected = Direction.SOUTH),
            data(Direction.NORTHEAST, expected = Direction.SOUTHWEST),
            data(Direction.SOUTHEAST, expected = Direction.NORTHWEST),
            data(Direction.SOUTH, expected = Direction.NORTH),
            data(Direction.SOUTHWEST, expected = Direction.NORTHEAST),
            data(Direction.NORTHWEST, expected = Direction.SOUTHEAST)
        ) { direction, expected ->
            it("should return $expected") {
                direction.opposite shouldBe expected
            }
        }
    }

    describe("minus") {
        on(
            "%s - %s",
            data(Direction.SOUTH, Angle(-6), expected = Direction.SOUTH),
            data(Direction.SOUTH, Angle(-5), expected = Direction.SOUTHEAST),
            data(Direction.SOUTH, Angle(-4), expected = Direction.NORTHEAST),
            data(Direction.SOUTH, Angle(-3), expected = Direction.NORTH),
            data(Direction.SOUTH, Angle(-2), expected = Direction.NORTHWEST),
            data(Direction.SOUTH, Angle(-1), expected = Direction.SOUTHWEST),
            data(Direction.SOUTH, Angle(0), expected = Direction.SOUTH),
            data(Direction.SOUTH, Angle(1), expected = Direction.SOUTHEAST),
            data(Direction.SOUTH, Angle(2), expected = Direction.NORTHEAST),
            data(Direction.SOUTH, Angle(3), expected = Direction.NORTH),
            data(Direction.SOUTH, Angle(4), expected = Direction.NORTHWEST),
            data(Direction.SOUTH, Angle(5), expected = Direction.SOUTHWEST),
            data(Direction.SOUTH, Angle(6), expected = Direction.SOUTH)
        ) { direction, angle, expected ->
            it("should return $expected") {
                direction - angle shouldEqual expected
            }
        }
    }

    describe("plus") {
        on(
            "%s + %s",
            data(Direction.SOUTH, Angle(-6), expected = Direction.SOUTH),
            data(Direction.SOUTH, Angle(-5), expected = Direction.SOUTHWEST),
            data(Direction.SOUTH, Angle(-4), expected = Direction.NORTHWEST),
            data(Direction.SOUTH, Angle(-3), expected = Direction.NORTH),
            data(Direction.SOUTH, Angle(-2), expected = Direction.NORTHEAST),
            data(Direction.SOUTH, Angle(-1), expected = Direction.SOUTHEAST),
            data(Direction.SOUTH, Angle(0), expected = Direction.SOUTH),
            data(Direction.SOUTH, Angle(1), expected = Direction.SOUTHWEST),
            data(Direction.SOUTH, Angle(2), expected = Direction.NORTHWEST),
            data(Direction.SOUTH, Angle(3), expected = Direction.NORTH),
            data(Direction.SOUTH, Angle(4), expected = Direction.NORTHEAST),
            data(Direction.SOUTH, Angle(5), expected = Direction.SOUTHEAST),
            data(Direction.SOUTH, Angle(6), expected = Direction.SOUTH)
        ) { direction, angle, expected ->
            it("should return $expected") {
                direction + angle shouldEqual expected
            }
        }
    }
})
