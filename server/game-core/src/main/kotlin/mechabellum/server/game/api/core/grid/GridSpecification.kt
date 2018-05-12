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
 * A specification for creating a new [Grid].
 *
 * @property deploymentZonesByTeam A collection of deployment zones on the grid for each team.
 * @property type The type of grid to create.
 *
 * @throws IllegalArgumentException If [deploymentZonesByTeam] does not have an entry for each team.
 */
data class GridSpecification(val deploymentZonesByTeam: Map<Team, CellIdRange>, val type: GridType) {
    init {
        checkAllTeamsHaveDeploymentZone()
        checkDeploymentZonesAreWithinGridBounds()
    }

    private fun checkAllTeamsHaveDeploymentZone() {
        Team.values().forEach { require(it in deploymentZonesByTeam) { "no deployment zone for team $it" } }
    }

    private fun checkDeploymentZonesAreWithinGridBounds() {
        val gridBounds = CellId(0, 0) until CellId(type.cols, type.rows)
        deploymentZonesByTeam.forEach { team, deploymentZone ->
            require((deploymentZone.start in gridBounds) && (deploymentZone.endInclusive in gridBounds)) {
                "deployment zone $deploymentZone for team $team exceeds grid bounds"
            }
        }
    }
}
