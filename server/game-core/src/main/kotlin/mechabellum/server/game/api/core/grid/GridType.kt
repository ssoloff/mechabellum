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

/**
 * The data describing a type of [Grid].
 *
 * @property cols The count of columns in the grid.
 * @property name The unique name for this type of grid (e.g. "Quick-Start Map").
 * @property rows The count of rows in the grid.
 *
 * @throws IllegalArgumentException If [cols] or [rows] is non-positive.
 */
data class GridType(
    val cols: Int,
    val name: String,
    val rows: Int
) {
    init {
        require(cols > 0) { "expected cols to be positive but was $cols" }
        require(rows > 0) { "expected rows to be positive but was $rows" }
    }
}
