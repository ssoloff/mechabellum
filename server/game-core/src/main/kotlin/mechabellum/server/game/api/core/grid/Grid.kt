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

import mechabellum.server.game.api.core.participant.Team

/**
 * A hexagonal grid.
 *
 * The grid uses an odd-q vertical layout. "odd-q" means each odd column is shifted down (positive y) a half-cell.
 * "Vertical" layout means each cell is a flat-topped hex (rather than a pointy-topped hex).
 */
interface Grid {
    /** The grid type. */
    val type: GridType

    /**
     * Returns the cell at the coordinates ([col], [row]).
     *
     * @throws IllegalArgumentException If no cell exists at the specified coordinates.
     */
    fun getCell(col: Int, row: Int): Cell

    /** Returns the deployment zone for the specified [team]. */
    fun getDeploymentZone(team: Team): CellRange
}
