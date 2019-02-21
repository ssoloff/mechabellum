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

package mechabellum.server.game.api.core

/**
 * Runs a BattleTech game.
 *
 * All game read and write operations are performed via a [GameRunner] by executing [Command]s.
 */
interface GameRunner {
    /**
     * Executes [command] synchronously in the context of the associated game.
     *
     * @throws IllegalArgumentException If the active game phase is not applicable for [command].
     * @throws GameException If a recoverable error occurs while running [command].
     * @throws UnexpectedCommandException If an unexpected checked exception is thrown while running [command].
     */
    fun <TPhase : Phase, R : Any> executeCommand(command: Command<TPhase, R>): R
}

/**
 * Factory for creating [GameRunner] implementations.
 *
 * Clients are expected to obtain an instance of this interface using the Java [java.util.ServiceLoader] framework.
 */
interface GameRunnerFactory {
    /** Returns a new [GameRunner] for the specified [GameSpecification]. */
    fun newGameRunner(specification: GameSpecification): GameRunner
}

/** An unchecked exception that indicates an unexpected checked exception was thrown when executing a command. */
class UnexpectedCommandException(cause: Throwable) : RuntimeException(
    "unexpected checked exception when executing command",
    cause
)
