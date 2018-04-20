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

package mechabellum.server.game.core.grid

/**
 * A direction on a hexagonal grid.
 */
enum class Direction(private val clockwiseOffsetFromNorth: Int) {
    NORTH(0),
    NORTHEAST(1),
    SOUTHEAST(2),
    SOUTH(3),
    SOUTHWEST(4),
    NORTHWEST(5);

    init {
        assert(clockwiseOffsetFromNorth >= 0)
    }

    /**
     * The direction immediately clockwise (-60 degrees) from this direction.
     */
    val clockwise: Direction
        get() = getDirectionAtRelativeClockwiseOffset(1)

    /**
     * The direction immediately counterclockwise (+60 degrees) from this direction.
     */
    val counterclockwise: Direction
        get() = getDirectionAtRelativeClockwiseOffset(DIRECTION_COUNT - 1)

    /**
     * The direction directly opposite (±180 degrees) from this direction.
     */
    val opposite: Direction
        get() = getDirectionAtRelativeClockwiseOffset(DIRECTION_COUNT / 2)

    private fun getDirectionAtRelativeClockwiseOffset(offset: Int): Direction {
        assert(offset >= 0)
        return DIRECTIONS_IN_CLOCKWISE_ORDER_FROM_NORTH[(clockwiseOffsetFromNorth + offset) % DIRECTION_COUNT]
    }

    private companion object {
        val DIRECTIONS_IN_CLOCKWISE_ORDER_FROM_NORTH = values().sortedBy(Direction::clockwiseOffsetFromNorth)
        val DIRECTION_COUNT = DIRECTIONS_IN_CLOCKWISE_ORDER_FROM_NORTH.size
    }
}
