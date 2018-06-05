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

package mechabellum.server.game.internal.core.phases

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultMessageFactory
import mechabellum.server.game.internal.core.DefaultPhase
import mechabellum.server.game.internal.core.unit.DefaultMech

internal class DefaultDeploymentPhase(
    game: DefaultGame,
    override val team: Team
) : DefaultPhase(game), DeploymentPhase {
    override fun deployMech(mech: Mech, position: Position) {
        checkMechBelongsToDeployingTeam(mech)
        checkPositionIsWithinTeamDeploymentPositions(position, mech.team)

        game.state.modifyMechRecord(mech.id) { it.setPosition(position) }
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
        game.state.mechRecords
            .filter { it.mech.team == team }
            .find { it.position is Option.None }
            ?.let { throw GameException(Messages.mechHasNotBeenDeployed(it.mech)) }
    }

    private interface Messages {
        fun mechHasNotBeenDeployed(mechId: MechId): String

        companion object : Messages by DefaultMessageFactory.get(Messages::class)
    }

    private fun Messages.mechHasNotBeenDeployed(mech: DefaultMech): String = mechHasNotBeenDeployed(mech.id)
}
