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

import kotlin.reflect.KClass

/** An abstract representation of some semantic game behavior. */
interface Command<R : Any, TPhase : Phase> {
    /** The type of phase during which the command may be run. */
    val phaseType: KClass<TPhase>

    /**
     * Returns the result of executing the command. The command can access the game state and behavior via the active
     * game [phase].
     *
     * @throws GameException If an error occurs while running the command.
     */
    fun execute(phase: TPhase): R
}

/**
 * Stateless implementation of [Command].
 *
 * This class is open simply to make it easier to implement a stateless command via inheritance rather than delegation,
 * which would require additional boilerplate in each implementation. For all intents and purposes, it should be
 * considered final.
 *
 * @param action The action to perform when the command is executed.
 */
open class StatelessCommand<R : Any, TPhase : Phase>(
    override val phaseType: KClass<TPhase>,
    private val action: (TPhase) -> R
) : Command<R, TPhase> {
    override fun execute(phase: TPhase): R = action(phase)
}
