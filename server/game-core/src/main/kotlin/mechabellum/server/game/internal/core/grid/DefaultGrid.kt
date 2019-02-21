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

package mechabellum.server.game.internal.core.grid

import mechabellum.server.game.api.core.grid.Cell
import mechabellum.server.game.api.core.grid.Grid
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.grid.PositionRange
import mechabellum.server.game.api.core.participant.Team

internal class DefaultGrid(specification: GridSpecification) : Grid {
    private val cellsByPosition: Map<Position, DefaultCell> = mutableMapOf<Position, DefaultCell>().apply {
        for (col in 0 until specification.type.cols) {
            for (row in 0 until specification.type.rows) {
                val position = Position(col, row)
                put(position, DefaultCell(position))
            }
        }
    }

    private val deploymentPositionsByTeam = specification.deploymentPositionsByTeam

    override val positions: PositionRange =
        Position(0, 0)..Position(specification.type.cols - 1, specification.type.rows - 1)

    override val type = specification.type

    override fun getCell(position: Position): Cell {
        return cellsByPosition.getOrElse(position) {
            throw IllegalArgumentException("expected cell at $position to be present but was absent")
        }
    }

    override fun getDeploymentPositions(team: Team): PositionRange = deploymentPositionsByTeam[team]!!
}
