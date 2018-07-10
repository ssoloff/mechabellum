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

import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Displacement
import mechabellum.server.game.api.core.grid.Position

internal data class CubePosition(val x: Int, val y: Int, val z: Int) {
    operator fun plus(displacement: Displacement): CubePosition = when (displacement.direction) {
        Direction.NORTH -> copy(y = y + displacement.magnitude, z = z - displacement.magnitude)
        Direction.NORTHEAST -> copy(x = x + displacement.magnitude, z = z - displacement.magnitude)
        Direction.SOUTHEAST -> copy(x = x + displacement.magnitude, y = y - displacement.magnitude)
        Direction.SOUTH -> copy(y = y - displacement.magnitude, z = z + displacement.magnitude)
        Direction.SOUTHWEST -> copy(x = x - displacement.magnitude, z = z + displacement.magnitude)
        Direction.NORTHWEST -> copy(x = x - displacement.magnitude, y = y + displacement.magnitude)
    }

    fun toOffsetPosition(): Position {
        val col = x
        val row = z + (x - col.parity) / 2
        return Position(col, row)
    }
}

private val Int.parity: Int
    get() = if ((this and 1) != 0) 1 else 0

internal fun Position.toCubePosition(): CubePosition {
    val x = col
    val z = row - (col - col.parity) / 2
    val y = -x - z
    return CubePosition(x, y, z)
}
