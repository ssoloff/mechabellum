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

package mechabellum.server.game.api.core.commands.deployment

import mechabellum.server.game.api.core.StatelessCommand
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.unit.Mech

/** Superclass for stateless commands that are executed during the deployment phase. */
open class StatelessDeploymentCommand<R : Any>(
    action: (DeploymentPhase) -> R
) : StatelessCommand<DeploymentPhase, R>(DeploymentPhase::class, action)

/**
 * Command that deploys a Mech to a position on the game grid with an initial facing.
 *
 * When executed, throws [IllegalArgumentException] if the Mech is not part of the game; if the Mech does not belong to
 * the team being deployed; or if the position is outside the possible deployment positions for the Mech.
 *
 * @param mech The Mech to deploy.
 * @param position The position on the game grid where the Mech will be deployed.
 * @param facing The initial facing of the Mech upon deployment.
 */
class DeployCommand(mech: Mech, position: Position, facing: Direction) : StatelessDeploymentCommand<Unit>({
    it.deploy(mech, position, facing)
})
