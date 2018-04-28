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
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.features.GridFeature
import mechabellum.server.game.api.core.grid.Grid

/** Returns the game grid. */
class GetGridCommand : Command<Grid> {
    override fun execute(context: CommandContext): Grid = getGridFeature(context).grid

    private fun getGridFeature(context: CommandContext): GridFeature {
        val featureOption = context.getFeature(GridFeature::class.java)
        return when (featureOption) {
            is Option.Some -> featureOption.value
            else -> throw CommandException("required feature 'GridFeature' not available")
        }
    }
}

/** The game grid. */
val Game.grid: Grid
    get() = executeCommand(GetGridCommand())
