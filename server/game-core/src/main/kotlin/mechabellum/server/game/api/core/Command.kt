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

package mechabellum.server.game.api.core

import mechabellum.server.common.api.core.util.Option

/** An abstract representation of some semantic game behavior. */
interface Command<T : Any> {
    /**
     * Returns the result of executing the command. The command can access the game state and behavior via [context].
     *
     * @throws CommandException If an error occurs while running the command.
     */
    fun execute(context: CommandContext): T
}

/**
 * The context within which a game command is executed.
 *
 * A command uses an instance of this context to access the game state and behavior.
 */
interface CommandContext {
    /** Returns the active game phase */
    fun getActivePhase(): Phase

    /** Returns the active game phase if it is of the specified [type]. */
    fun <T : Phase> getActivePhaseAs(type: Class<T>): Option<T>
}

/** A checked exception that indicates an error occurred while executing a command. */
class CommandException(message: String) : Exception(message)
