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

package mechabellum.server.game.api.core.grid

/**
 * A displacement between two [Cell]s.
 *
 * @property magnitude The magnitude of the displacement in cells.
 * @property direction The direction of the displacement from the initial cell to the final cell.
 *
 * @throws IllegalArgumentException If [magnitude] is negative.
 */
data class Displacement(val magnitude: Int, val direction: Direction) {
    init {
        require(magnitude >= 0) { "expected magnitude to be non-negative but was $magnitude" }
    }
}
