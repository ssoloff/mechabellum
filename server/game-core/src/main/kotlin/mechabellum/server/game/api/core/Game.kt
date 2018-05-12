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
     * Executes [command] synchronously and returns its result.
     *
     * @throws IllegalArgumentException If the active game phase is not applicable for [command].
     * @throws UnexpectedCheckedException If [command] throws an unexpected checked exception.
     */
    fun <R : Any, TPhase : Phase> executeCommand(command: Command<R, TPhase>): CommandResult<R>
}

/** A checked exception that indicates an error occurred within a game. */
class GameException(message: String) : Exception(message)

// TODO: consider moving this to common-core
/** An unchecked exception that indicates an unexpected checked exception was thrown while executing a game command. */
class UnexpectedCheckedException(cause: Exception) : RuntimeException("unexpected checked exception", cause)
