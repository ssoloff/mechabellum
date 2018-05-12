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

package mechabellum.server.game.api.core.commands.general

import mechabellum.server.common.api.core.util.Result
import mechabellum.server.game.api.core.CommandResult
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.StatelessCommand
import mechabellum.server.game.api.core.grid.Grid

/** Superclass for stateless commands that are executed during any phase. */
open class StatelessGeneralCommand<R : Any>(
    action: (Phase) -> CommandResult<R>
) : StatelessCommand<R, Phase>(Phase::class, action)

/** The game grid. */
val Game.grid: CommandResult<Grid>
    get() = executeCommand(GetGridCommand())

class GetGridCommand : StatelessGeneralCommand<Grid>({
    Result.success(it.grid)
})
