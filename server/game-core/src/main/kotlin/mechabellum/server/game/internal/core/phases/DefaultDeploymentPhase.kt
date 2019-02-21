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

package mechabellum.server.game.internal.core.phases

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultPhase

internal class DefaultDeploymentPhase(
    game: DefaultGame,
    override val team: Team
) : DefaultPhase(game), DeploymentPhase {
    override fun deploy(mech: Mech, position: Position, facing: Direction) {
        checkMechBelongsToDeployingTeam(mech)
        checkPositionIsWithinTeamDeploymentPositions(position, mech.team)

        game.state.modifyMech(mech.id) { it.setFacing(facing).setPosition(position) }
    }

    private fun checkMechBelongsToDeployingTeam(mech: Mech) {
        require(mech.team == team) { "expected team $team to be deployed during this phase but was ${mech.team}" }
    }

    private fun checkPositionIsWithinTeamDeploymentPositions(position: Position, team: Team) {
        val deploymentPositions = game.grid.getDeploymentPositions(team)
        require(position in deploymentPositions) {
            "expected position $position to be present in deployment positions $deploymentPositions for team $team but was absent"
        }
    }

    override fun end() {
        checkAllTeamMechsDeployed()

        game.phase = when (team) {
            Team.DEFENDER -> DefaultDeploymentPhase(game, Team.ATTACKER)
            Team.ATTACKER -> DefaultInitiativePhase(game, game.state.addTurn())
        }
    }

    private fun checkAllTeamMechsDeployed() {
        val undeployedMechIds = game.state.mechs
            .filter { it.team == team }
            .filter { it.position is Option.None }
            .map { it.id }
        check(undeployedMechIds.isEmpty()) { "Mechs $undeployedMechIds from team $team have not been deployed" }
    }
}
