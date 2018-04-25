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

package mechabellum.server.game.internal.core.grid

import mechabellum.server.game.api.core.grid.Cell
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.grid.Grid

internal class InternalGrid(override val cols: Int, override val rows: Int) : Grid {
    private val _cellsById: Map<CellId, InternalCell> = mutableMapOf<CellId, InternalCell>().apply {
        for (col in 0 until cols) {
            for (row in 0 until rows) {
                val cellId = CellId(col, row)
                put(cellId, InternalCell(cellId))
            }
        }
    }

    override fun getCell(col: Int, row: Int): Cell {
        return _cellsById.getOrElse(CellId(col, row)) {
            throw IllegalArgumentException("cell ($col, $row) does not exist")
        }
    }
}
