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
import mechabellum.server.game.api.core.grid.CellIdRange
import mechabellum.server.game.api.core.grid.Grid
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.participant.Team

internal class DefaultGrid(specification: GridSpecification) : Grid {
    private val cellsById: Map<CellId, DefaultCell> = mutableMapOf<CellId, DefaultCell>().apply {
        for (col in 0 until specification.type.cols) {
            for (row in 0 until specification.type.rows) {
                val cellId = CellId(col, row)
                put(cellId, DefaultCell(cellId))
            }
        }
    }

    private val deploymentZonesByTeam = specification.deploymentZonesByTeam

    override val type = specification.type

    override fun getCell(col: Int, row: Int): Cell {
        return cellsById.getOrElse(CellId(col, row)) {
            throw IllegalArgumentException("cell ($col, $row) does not exist")
        }
    }

    override fun getDeploymentZone(team: Team): CellIdRange = deploymentZonesByTeam[team]!!
}
