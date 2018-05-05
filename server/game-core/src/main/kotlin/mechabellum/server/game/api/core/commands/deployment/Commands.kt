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

package mechabellum.server.game.api.core.commands.deployment

import mechabellum.server.game.api.core.Command
import mechabellum.server.game.api.core.CommandContext
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.getPhaseAs
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.unit.Mech

/** Deploys [mech] to the specified [position]. */
class DeployMechCommand(private val mech: Mech, private val position: CellId) : Command<Unit> {
    /**
     * @throws IllegalArgumentException If [mech] is not part of this game; or if [position] is outside the deployment
     * zone for [mech].
     */
    override fun execute(context: CommandContext) = context
        .getPhaseAs(DeploymentPhase::class.java)
        .deployMech(mech, position)
}

/**
 * Deploys [mech] to the specified [position].
 *
 * @throws IllegalArgumentException If [mech] is not part of this game; or if [position] is outside the deployment
 * zone for [mech].
 */
fun Game.deployMech(mech: Mech, position: CellId) = executeCommand(DeployMechCommand(mech, position))
