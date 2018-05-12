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

/** A BattleTech game session. */
interface Game {
    /**
     * Executes [command] synchronously.
     *
     * @throws IllegalArgumentException If the active game phase is not applicable for [command].
     * @throws GameException If a recoverable error occurs while running [command].
     * @throws UnexpectedGameException If an unexpected checked exception is thrown while running [command].
     */
    fun <R : Any, TPhase : Phase> executeCommand(command: Command<R, TPhase>): R
}

/** A checked exception that indicates an error occurred within a game. */
class GameException(message: String) : Exception(message)

/** An unchecked exception that indicates an unexpected checked exception was thrown when running a command. */
class UnexpectedCommandException(cause: Throwable) : RuntimeException(
    "unexpected checked exception when running command",
    cause
)
