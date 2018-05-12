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

import mechabellum.server.game.api.core.CommandResult
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.StatelessCommand
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.unit.Mech

/** Superclass for stateless commands that are executed during the deployment phase. */
open class StatelessDeploymentCommand<R : Any>(
    action: (DeploymentPhase) -> R
) : StatelessCommand<R, DeploymentPhase>(DeploymentPhase::class, action)

/**
 * Deploys [mech] to the specified [position].
 *
 * @throws IllegalArgumentException If [mech] is not part of this game; or if [position] is outside the possible
 * deployment positions for [mech].
 */
fun Game.deployMech(mech: Mech, position: Position): CommandResult<Unit> =
    executeCommand(DeployMechCommand(mech, position))

class DeployMechCommand(mech: Mech, position: Position) : StatelessDeploymentCommand<Unit>({
    it.deployMech(mech, position)
})
