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

/**
 * A direction on a hexagonal grid.
 */
enum class Direction {
    NORTH,
    NORTHEAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    NORTHWEST;

    /**
     * The direction immediately clockwise (-60 degrees) from this direction.
     */
    val clockwise: Direction
        get() = getDirectionAtOffset(1)

    /**
     * The direction immediately counterclockwise (+60 degrees) from this direction.
     */
    val counterclockwise: Direction
        get() = getDirectionAtOffset(DIRECTION_COUNT - 1)

    /**
     * The direction directly opposite (±180 degrees) from this direction.
     */
    val opposite: Direction
        get() = getDirectionAtOffset(DIRECTION_COUNT / 2)

    private fun getDirectionAtOffset(offset: Int): Direction {
        assert(offset >= 0)
        return DIRECTIONS[(ordinal + offset) % DIRECTION_COUNT]
    }

    private companion object {
        val DIRECTIONS = values()
        val DIRECTION_COUNT = DIRECTIONS.size
    }
}
