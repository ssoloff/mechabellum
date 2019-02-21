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

import mechabellum.server.game.api.core.participant.Team

/**
 * A specification for creating a new [Grid].
 *
 * @property deploymentPositionsByTeam The possible deployment positions on the grid for each team.
 * @property type The type of grid to create.
 *
 * @throws IllegalArgumentException If [deploymentPositionsByTeam] does not have an entry for each team.
 */
data class GridSpecification(val deploymentPositionsByTeam: Map<Team, PositionRange>, val type: GridType) {
    init {
        checkAllTeamsHaveDeploymentPositions()
        checkDeploymentPositionsAreWithinGridBounds()
    }

    private fun checkAllTeamsHaveDeploymentPositions() {
        Team.values().forEach {
            require(it in deploymentPositionsByTeam) { "expected deployment positions for team $it to be present but was absent" }
        }
    }

    private fun checkDeploymentPositionsAreWithinGridBounds() {
        val gridBounds = Position(0, 0) until Position(type.cols, type.rows)
        deploymentPositionsByTeam.forEach { team, deploymentPositions ->
            require((deploymentPositions.start in gridBounds) && (deploymentPositions.endInclusive in gridBounds)) {
                "expected deployment positions $deploymentPositions for team $team to be within grid bounds but exceeded grid bounds"
            }
        }
    }
}
