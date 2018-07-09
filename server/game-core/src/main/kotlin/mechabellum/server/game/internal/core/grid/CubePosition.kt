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

import mechabellum.server.game.api.core.grid.Position

internal data class CubePosition(val x: Int, val y: Int, val z: Int) {
    fun toOffsetPosition(): Position {
        val col = x
        val row = z + (x - (if (isOddColumn(col)) 1 else 0)) / 2
        return Position(col, row)
    }

    companion object {
        fun fromOffsetPosition(offsetPosition: Position): CubePosition {
            val x = offsetPosition.col
            val z = offsetPosition.row - (offsetPosition.col - (if (isOddColumn(offsetPosition.col)) 1 else 0)) / 2
            val y = -x - z
            return CubePosition(x, y, z)
        }

        private fun isOddColumn(col: Int): Boolean = (col and 1) != 0
    }
}
