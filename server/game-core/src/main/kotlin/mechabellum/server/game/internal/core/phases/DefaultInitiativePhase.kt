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

package mechabellum.server.game.internal.core.phases

import mechabellum.server.common.api.core.util.getOrThrow
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.InitiativePhase
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultPhase

internal class DefaultInitiativePhase(
    game: DefaultGame,
    private val turnId: TurnId
) : DefaultPhase(game), InitiativePhase {
    override fun end() {
        game.phase = DefaultMovementPhase(
            game,
            game.state.getTurn(turnId).initiativeWinner.getOrThrow {
                IllegalStateException("expected initiative to be rolled but was not")
            }
        )
    }

    override fun rollInitiative() {
        game.state.modifyTurn(turnId) { it.setInitiatives(rollInitiativeAndResolveTies()) }
    }

    private fun rollInitiativeAndResolveTies(): Map<Team, Initiative> {
        while (true) {
            val initiativesByTeam = Team.values().associate {
                it to Initiative(game.dieRoller.roll() + game.dieRoller.roll())
            }
            val maxInitiatives: Initiative = initiativesByTeam.values.max()!!
            if (initiativesByTeam.values.count(maxInitiatives::equals) == 1) {
                return initiativesByTeam
            }
        }
    }
}
