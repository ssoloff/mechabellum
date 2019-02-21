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

package mechabellum.server.game.internal.core.grid

import mechabellum.server.common.api.test.DataClassSpec
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Displacement
import mechabellum.server.game.api.core.grid.Position
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

object CubePositionSpec : Spek({
    describe("plus") {
        on(
            "${CubePosition(0, 0, 0)} + %s",
            data(Displacement(0, Direction.NORTH), expected = CubePosition(0, 0, 0)),
            data(Displacement(1, Direction.NORTH), expected = CubePosition(0, 1, -1)),
            data(Displacement(1, Direction.NORTHEAST), expected = CubePosition(1, 0, -1)),
            data(Displacement(1, Direction.SOUTHEAST), expected = CubePosition(1, -1, 0)),
            data(Displacement(1, Direction.SOUTH), expected = CubePosition(0, -1, 1)),
            data(Displacement(1, Direction.SOUTHWEST), expected = CubePosition(-1, 0, 1)),
            data(Displacement(1, Direction.NORTHWEST), expected = CubePosition(-1, 1, 0)),
            data(Displacement(2, Direction.NORTH), expected = CubePosition(0, 2, -2)),
            data(Displacement(2, Direction.NORTHEAST), expected = CubePosition(2, 0, -2)),
            data(Displacement(2, Direction.SOUTHEAST), expected = CubePosition(2, -2, 0)),
            data(Displacement(2, Direction.SOUTH), expected = CubePosition(0, -2, 2)),
            data(Displacement(2, Direction.SOUTHWEST), expected = CubePosition(-2, 0, 2)),
            data(Displacement(2, Direction.NORTHWEST), expected = CubePosition(-2, 2, 0))
        ) { displacement, expected ->
            it("should return $expected") {
                CubePosition(0, 0, 0) + displacement shouldEqual expected
            }
        }
    }

    describe("toOffsetPosition") {
        on(
            "%s",
            data(CubePosition(0, 0, 0), expected = Position(0, 0)),
            data(CubePosition(0, 1, -1), expected = Position(0, -1)),
            data(CubePosition(1, 0, -1), expected = Position(1, -1)),
            data(CubePosition(1, -1, 0), expected = Position(1, 0)),
            data(CubePosition(0, -1, 1), expected = Position(0, 1)),
            data(CubePosition(-1, 0, 1), expected = Position(-1, 0)),
            data(CubePosition(-1, 1, 0), expected = Position(-1, -1)),
            data(CubePosition(0, 2, -2), expected = Position(0, -2)),
            data(CubePosition(1, 1, -2), expected = Position(1, -2)),
            data(CubePosition(2, 0, -2), expected = Position(2, -1)),
            data(CubePosition(2, -1, -1), expected = Position(2, 0)),
            data(CubePosition(2, -2, 0), expected = Position(2, 1)),
            data(CubePosition(1, -2, 1), expected = Position(1, 1)),
            data(CubePosition(0, -2, 2), expected = Position(0, 2)),
            data(CubePosition(-1, -1, 2), expected = Position(-1, 1)),
            data(CubePosition(-2, 0, 2), expected = Position(-2, 1)),
            data(CubePosition(-2, 1, 1), expected = Position(-2, 0)),
            data(CubePosition(-2, 2, 0), expected = Position(-2, -1)),
            data(CubePosition(-1, 2, -1), expected = Position(-1, -2))
        ) { cubePosition, expected ->
            it("should return $expected") {
                cubePosition.toOffsetPosition() shouldEqual expected
            }
        }
    }
})

object CubePositionBehavesAsDataClassSpec : DataClassSpec({ CubePosition(11, 22, 33) })

object CubePositionExtensionsForPositionSpec : Spek({
    describe("toCubePosition") {
        on(
            "%s",
            data(Position(0, 0), expected = CubePosition(0, 0, 0)),
            data(Position(0, -1), expected = CubePosition(0, 1, -1)),
            data(Position(1, -1), expected = CubePosition(1, 0, -1)),
            data(Position(1, 0), expected = CubePosition(1, -1, 0)),
            data(Position(0, 1), expected = CubePosition(0, -1, 1)),
            data(Position(-1, 0), expected = CubePosition(-1, 0, 1)),
            data(Position(-1, -1), expected = CubePosition(-1, 1, 0)),
            data(Position(0, -2), expected = CubePosition(0, 2, -2)),
            data(Position(1, -2), expected = CubePosition(1, 1, -2)),
            data(Position(2, -1), expected = CubePosition(2, 0, -2)),
            data(Position(2, 0), expected = CubePosition(2, -1, -1)),
            data(Position(2, 1), expected = CubePosition(2, -2, 0)),
            data(Position(1, 1), expected = CubePosition(1, -2, 1)),
            data(Position(0, 2), expected = CubePosition(0, -2, 2)),
            data(Position(-1, 1), expected = CubePosition(-1, -1, 2)),
            data(Position(-2, 1), expected = CubePosition(-2, 0, 2)),
            data(Position(-2, 0), expected = CubePosition(-2, 1, 1)),
            data(Position(-2, -1), expected = CubePosition(-2, 2, 0)),
            data(Position(-1, -2), expected = CubePosition(-1, 2, -1))
        ) { offsetPosition, expected ->
            it("should return $expected") {
                offsetPosition.toCubePosition() shouldEqual expected
            }
        }
    }
})
