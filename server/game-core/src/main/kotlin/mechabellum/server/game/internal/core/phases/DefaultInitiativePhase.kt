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

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.common.api.core.util.getOrThrow
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.InitiativePhase
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultPhase
import mechabellum.server.game.internal.core.DefaultTurn

internal class DefaultInitiativePhase(
    game: DefaultGame,
    private val turnId: TurnId
) : DefaultPhase(game), InitiativePhase {
    private val turn: DefaultTurn
        get() = game.state.getTurn(turnId)

    override fun end() {
        checkAllTeamsHaveRolledInitiative()
        val initiativeWinner = checkInitiativeWinnerExists()

        game.phase = DefaultMovementPhase(game, initiativeWinner)
    }

    private fun checkAllTeamsHaveRolledInitiative() = turn.teamsWithoutInitiative.let {
        if (it.isNotEmpty()) {
            throw IllegalStateException("team(s) $it have not rolled initiative")
        }
    }

    private fun checkInitiativeWinnerExists(): Team = turn.initiativeWinner.getOrThrow {
        IllegalStateException("all teams rolled initiative but there is no winner; each team must re-roll")
    }

    override fun rollInitiative(team: Team) {
        checkTeamCanRollInitiative(team)

        game.state.modifyTurn(turnId) {
            it.setInitiative(team, Initiative(game.dieRoller.roll() + game.dieRoller.roll()))
        }
    }

    private fun checkTeamCanRollInitiative(team: Team) {
        if ((turn.initiativeRollsIncomplete && (turn.getInitiative(team) is Option.Some)) ||
            (turn.initiativeWinner is Option.Some)
        ) {
            throw IllegalStateException("illegal attempt to re-roll initiative for team $team")
        }
    }
}
