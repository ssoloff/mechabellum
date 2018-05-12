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
     * @throws RuntimeException If [command] fails by throwing an unchecked exception (the thrown exception is the
     * original unchecked exception thrown by the command and thus may be a subclass of [RuntimeException]).
     * @throws GameException If [command] fails by throwing a checked exception (the original exception thrown by the
     * command will be the cause).
     */
    // TODO: NEXT... change to return Result<R, GameException> ??
    // - document that unchecked exceptions thrown while executing command will be rethrown
    // - checked exceptions are returned through the Result
    //
    // TODO: how should we handle Empty??
    // the only way that will work properly is if we have Command also return Result<R, GameException>
    //
    // bottom line is this forces even the command extension functions to deal with Results, which may not be what we want
    // let's see how the TrainingScenarioSpec looks after making this change (only going to use Result here; not in the commands)
    //
    // - BASICALLY, this makes the extension functions useless (which they may have been to begin with; especially in light
    // of possibly renaming Game to GameRunner)
    fun <R : Any, TPhase : Phase> executeCommand(command: Command<R, TPhase>): CommandResult<R>
}

/** A checked exception that indicates an error occurred within a game. */
class GameException : Exception {
    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}
