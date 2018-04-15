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

package mechabellum.server.game.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DirectionTest {
    @Test
    fun `should define exactly six directions`() {
        assertEquals(6, Direction.values().size)
    }

    @Test
    fun `should provide direction immediately clockwise`() {
        assertEquals(Direction.NORTHEAST, Direction.NORTH.clockwise)
        assertEquals(Direction.SOUTHEAST, Direction.NORTHEAST.clockwise)
        assertEquals(Direction.SOUTH, Direction.SOUTHEAST.clockwise)
        assertEquals(Direction.SOUTHWEST, Direction.SOUTH.clockwise)
        assertEquals(Direction.NORTHWEST, Direction.SOUTHWEST.clockwise)
        assertEquals(Direction.NORTH, Direction.NORTHWEST.clockwise)
    }

    @Test
    fun `should provide direction immediately counterclockwise`() {
        assertEquals(Direction.NORTHWEST, Direction.NORTH.counterclockwise)
        assertEquals(Direction.NORTH, Direction.NORTHEAST.counterclockwise)
        assertEquals(Direction.NORTHEAST, Direction.SOUTHEAST.counterclockwise)
        assertEquals(Direction.SOUTHEAST, Direction.SOUTH.counterclockwise)
        assertEquals(Direction.SOUTH, Direction.SOUTHWEST.counterclockwise)
        assertEquals(Direction.SOUTHWEST, Direction.NORTHWEST.counterclockwise)
    }

    @Test
    fun `should provide direction directly opposite`() {
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite)
        assertEquals(Direction.SOUTHWEST, Direction.NORTHEAST.opposite)
        assertEquals(Direction.NORTHWEST, Direction.SOUTHEAST.opposite)
        assertEquals(Direction.NORTH, Direction.SOUTH.opposite)
        assertEquals(Direction.NORTHEAST, Direction.SOUTHWEST.opposite)
        assertEquals(Direction.SOUTHEAST, Direction.NORTHWEST.opposite)
    }
}
