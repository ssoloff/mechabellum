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

import mechabellum.server.game.internal.core.grid.toCubePosition

/**
 * A position on a hexagonal grid.
 *
 * @property col The grid column.
 * @property row The grid row.
 */
data class Position(val col: Int, val row: Int) : Comparable<Position> {
    override fun compareTo(other: Position): Int = COMPARATOR.compare(this, other)

    /** Returns the position relative to this position after adding [displacement]. */
    operator fun plus(displacement: Displacement): Position = (toCubePosition() + displacement).toOffsetPosition()

    /** Creates a range from this value to the specified [other] value. */
    operator fun rangeTo(other: Position): PositionRange = PositionRange(this, other)

    companion object {
        private val COMPARATOR = Comparator.comparingInt(Position::col).thenComparingInt(Position::row)
    }
}

/** Returns a range from this value up to but excluding the specified [to] value. */
infix fun Position.until(to: Position): PositionRange = PositionRange(this, Position(to.col - 1, to.row - 1))

/**
 * A rectangular range of grid positions.
 *
 * @property start The minimum value in the range.
 * @property endInclusive The maximum value in the range (inclusive).
 */
data class PositionRange(override val start: Position, override val endInclusive: Position) : ClosedRange<Position>
