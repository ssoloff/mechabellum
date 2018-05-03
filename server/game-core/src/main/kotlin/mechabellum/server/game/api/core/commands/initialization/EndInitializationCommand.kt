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

package mechabellum.server.game.api.core.commands.initialization

import mechabellum.server.game.api.core.Command
import mechabellum.server.game.api.core.CommandContext
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.getPhaseAs
import mechabellum.server.game.api.core.phases.InitializationPhase

/** Ends the initialization phase. */
class EndInitializationCommand : Command<Unit> {
    /** @throws mechabellum.server.game.api.core.GameException If all teams do not have at least one Mech. */
    override fun execute(context: CommandContext) = context
        .getPhaseAs(InitializationPhase::class.java)
        .end()
}

/**
 * Ends the initialization phase.
 *
 * @throws mechabellum.server.game.api.core.GameException If all teams do not have at least one Mech.
 */
fun Game.endInitialization() = executeCommand(EndInitializationCommand())
