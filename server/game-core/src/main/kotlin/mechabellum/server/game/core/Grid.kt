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

import mechabellum.server.game.internal.core.InternalGrid

/**
 * A hexagonal grid.
 *
 * The grid uses an odd-q vertical layout. "odd-q" means each odd column is shifted down (positive y) a half-cell.
 * "Vertical" layout means each cell is a flat-topped hex (rather than a pointy-topped hex).
 */
interface Grid {
    /** The count of columns in the grid. */
    val cols: Int

    /** The count of rows in the grid. */
    val rows: Int

    /**
     * Returns the cell at the coordinates ([col], [row]).
     *
     * @throws IllegalArgumentException If no cell exists at the specified coordinates.
     */
    fun getCell(col: Int, row: Int): Cell

    companion object {
        /**
         * Returns a new grid of size [cols] by [rows]. The first cell will be at offset (0, 0). The last cell will be
         * at offset ([cols]-1, [rows]-1).
         *
         * @throws IllegalArgumentException If [cols] or [rows] is not positive.
         */
        // TODO: eventually replace factory method with a service
        fun newInstance(cols: Int, rows: Int): Grid {
            require(cols > 0) { "cols must be positive" }
            require(rows > 0) { "rows must be positive" }

            return InternalGrid(cols, rows)
        }
    }
}
