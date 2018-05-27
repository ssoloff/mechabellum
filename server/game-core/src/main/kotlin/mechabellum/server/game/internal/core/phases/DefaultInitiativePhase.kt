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

import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.InitiativePhase
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultPhase

internal class DefaultInitiativePhase(game: DefaultGame) : DefaultPhase(game), InitiativePhase {
    private val initiativeRollsByTeam: Map<Team, Int> = rollInitiative()

    private fun rollInitiative(): Map<Team, Int> {
        while (true) {
            val initiativeRollsByTeam = Team.values().associate {
                it to (game.dieRoller.roll() + game.dieRoller.roll())
            }
            val maxInitiativeRoll: Int = initiativeRollsByTeam.values.max()!!
            if (initiativeRollsByTeam.values.count(maxInitiativeRoll::equals) == 1) {
                return initiativeRollsByTeam
            }
        }
    }

    override fun end() {
        game.phase = DefaultMovementPhase(game, teamWithInitiative)
    }

    private val teamWithInitiative: Team
        get() = initiativeRollsByTeam.maxBy(Map.Entry<Team, Int>::value)!!.key

    override fun getInitiativeRoll(team: Team): Int = initiativeRollsByTeam[team]!!
}
