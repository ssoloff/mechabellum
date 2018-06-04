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

import mechabellum.server.game.api.core.Turn
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.participant.Team

internal class DefaultTurn(
    override val id: TurnId,
    private val initiativeRollsByTeam: Map<Team, Int> = DEFAULT_INITIATIVE_ROLLS_BY_TEAM
) : Turn {
    init {
        checkAllTeamsHaveInitiativeRoll()
        checkOneTeamHasInitiative()
    }

    private fun checkAllTeamsHaveInitiativeRoll() {
        Team.values().forEach { require(it in initiativeRollsByTeam) { "no initiative roll for team $it" } }
    }

    private fun checkOneTeamHasInitiative() {
        val maxInitiativeRoll: Int = initiativeRollsByTeam.values.max()!!
        require(initiativeRollsByTeam.values.count(maxInitiativeRoll::equals) == 1) { "no team with initiative" }
    }

    val teamWithInitiative: Team = initiativeRollsByTeam.maxBy(Map.Entry<Team, Int>::value)!!.key

    override fun getInitiativeRoll(team: Team): Int = initiativeRollsByTeam[team]!!

    fun setInitiativeRolls(initiativeRollsByTeam: Map<Team, Int>): DefaultTurn = DefaultTurn(
        id = id,
        initiativeRollsByTeam = initiativeRollsByTeam
    )

    companion object {
        private val DEFAULT_INITIATIVE_ROLLS_BY_TEAM: Map<Team, Int> = defaultInitiativeRolls()

        private fun defaultInitiativeRolls(): Map<Team, Int> {
            val initiativeRollsByTeam: MutableMap<Team, Int> = Team.values().associate { it to 2 }.toMutableMap()
            initiativeRollsByTeam[Team.values().first()] = 12
            return initiativeRollsByTeam
        }
    }
}
