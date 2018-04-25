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

package mechabellum.server.game.api.core.commands

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.Command
import mechabellum.server.game.api.core.CommandContext
import mechabellum.server.game.api.core.CommandException
import mechabellum.server.game.api.core.features.DeploymentFeature
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.unit.Mech

/** Deploys [mech] to [position] on the game grid. */
class DeployMechCommand(private val mech: Mech, private val position: CellId) : Command<Unit> {
    override fun execute(context: CommandContext) {
        val deploymentFeature = getDeploymentFeature(context)
        deploymentFeature.deployMech(mech, position)
    }

    private fun getDeploymentFeature(context: CommandContext): DeploymentFeature {
        val featureOption = context.getFeature(DeploymentFeature::class.java)
        return when (featureOption) {
            is Option.Some -> featureOption.value
            else -> throw CommandException("required feature 'DeploymentFeature' not available")
        }
    }
}
