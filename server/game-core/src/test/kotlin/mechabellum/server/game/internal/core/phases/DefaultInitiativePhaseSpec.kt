// ktlint-disable filename

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

package mechabellum.server.game.internal.core.phases

import mechabellum.server.game.api.core.phases.InitiativePhaseSpec
import mechabellum.server.game.internal.core.DefaultGame

object DefaultInitiativePhaseBehavesAsInitializationPhaseSpec : InitiativePhaseSpec(
    newStrategy = { gameSpecification ->
        object : Strategy {
            override val game: DefaultGame = DefaultGame(gameSpecification)

            init {
                game.state.addTurn()
            }
        }
    },
    newSubject = { strategy ->
        val game = strategy.game as DefaultGame
        DefaultInitiativePhase(game, game.turn.id)
    }
)
