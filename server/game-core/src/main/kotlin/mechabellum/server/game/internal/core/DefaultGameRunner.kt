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

package mechabellum.server.game.internal.core

import mechabellum.server.game.api.core.Command
import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.GameRunner
import mechabellum.server.game.api.core.GameRunnerFactory
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.UnexpectedCommandException
import kotlin.reflect.full.cast

internal class DefaultGameRunner(private val game: DefaultGame) : GameRunner {
    override fun <TPhase : Phase, R : Any> executeCommand(command: Command<TPhase, R>): R {
        require(command.phaseType.isInstance(game.phase)) {
            "expected phase ${command.phaseType.simpleName} to be active but ${game.phase.javaClass.simpleName} was active"
        }

        try {
            return command.execute(command.phaseType.cast(game.phase))
        } catch (e: RuntimeException) {
            throw e
        } catch (e: GameException) {
            throw e
        } catch (e: Exception) {
            throw UnexpectedCommandException(e)
        }
    }
}

internal class DefaultGameRunnerFactory : GameRunnerFactory {
    override fun newGameRunner(specification: GameSpecification): GameRunner = DefaultGameRunner(
        DefaultGame(specification)
    )
}
