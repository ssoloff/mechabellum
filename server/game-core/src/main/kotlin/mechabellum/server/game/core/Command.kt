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

package mechabellum.server.game.core

/**
 * An abstract representation of some semantic game behavior.
 */
interface Command<T : Any> {
    /**
     * Returns the result of executing the command. The command can access the game state and behavior via [context].
     */
    fun execute(context: CommandContext): T
}

/**
 * The context within which a game command is executed.
 *
 * A command uses an instance of this context to access the game state and behavior.
 */
interface CommandContext {
    // TODO: provide the ability to access arbitrary "extension" or "feature" interfaces provided by the game
}
