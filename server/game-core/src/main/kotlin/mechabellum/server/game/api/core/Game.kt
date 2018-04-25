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
     * @throws GameException If [command] fails by throwing a checked exception (the original exception thrown by the
     * command will be the cause). If [command] fails by throwing an unchecked exception, that exception will be thrown
     * directly.
     */
    fun <T : Any> executeCommand(command: Command<T>): T
}

/** A checked exception that indicates an error occurred within a game. */
class GameException(message: String, cause: Throwable) : Exception(message, cause)
