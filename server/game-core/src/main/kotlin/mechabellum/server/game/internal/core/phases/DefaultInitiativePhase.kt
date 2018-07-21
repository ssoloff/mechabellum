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
import mechabellum.server.game.internal.core.DefaultTurnPhase

internal class DefaultInitiativePhase(
    game: DefaultGame,
    turnId: TurnId
) : DefaultTurnPhase(game, turnId), InitiativePhase {
    override fun end() {
        checkAllTeamsHaveRolledInitiative()
        val initiativeWinner = checkInitiativeWinnerExists()

        game.phase = DefaultMovementPhase(game = game, team = initiativeWinner.opponent, turnId = turnId)
    }

    private fun checkAllTeamsHaveRolledInitiative() = turn.initiativeHistory.teamsWithoutInitiative.let {
        check(it.isEmpty()) { "team(s) $it have not rolled initiative" }
    }

    private fun checkInitiativeWinnerExists(): Team = turn.initiativeHistory.initiativeWinner.getOrThrow {
        IllegalStateException("all teams rolled initiative but there is no winner; each team must re-roll")
    }

    override fun rollInitiative(team: Team): Initiative {
        checkTeamCanRollInitiative(team)

        val initiative = Initiative(game.dieRoller.roll() + game.dieRoller.roll())
        game.state.modifyTurn(turnId) { it.setInitiativeHistory(it.initiativeHistory.setInitiative(team, initiative)) }
        return initiative
    }

    private fun checkTeamCanRollInitiative(team: Team) {
        turn.initiativeHistory.let {
            check(
                if (it.initiativeRollsComplete) {
                    it.initiativeWinner is Option.None
                } else {
                    it.getInitiative(team) is Option.None
                }
            ) { "illegal attempt to re-roll initiative for team $team" }
        }
    }
}
