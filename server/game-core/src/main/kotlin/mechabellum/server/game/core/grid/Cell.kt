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
 * The unique identifier of a cell in a hexagonal grid.
 *
 * @property col The cell column.
 * @property row The cell row.
 */
data class CellId(val col: Int, val row: Int)

/** A cell in a hexagonal grid. */
interface Cell {
    /** The cell identifier. */
    val id: CellId
}
