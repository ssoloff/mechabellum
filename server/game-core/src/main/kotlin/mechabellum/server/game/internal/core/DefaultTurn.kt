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
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team

internal class DefaultTurn(
    override val id: TurnId,
    private val initiativesByTeam: Map<Team, Initiative> = DEFAULT_INITIATIVES_BY_TEAM
) : Turn {
    init {
        checkAllTeamsHaveInitiative()
        checkInitiativeWinnerExists()
    }

    private fun checkAllTeamsHaveInitiative() = Team.values().forEach {
        require(it in initiativesByTeam) { "expected initiative for team $it but was absent" }
    }

    private fun checkInitiativeWinnerExists() {
        val maxInitiative: Initiative = initiativesByTeam.values.max()!!
        val teamsWithMaxInitiative = initiativesByTeam.values.count(maxInitiative::equals)
        require(teamsWithMaxInitiative == 1) {
            "expected 1 team to have the max initiative ($maxInitiative) but was $teamsWithMaxInitiative teams"
        }
    }

    val initiativeWinner: Team = initiativesByTeam.maxBy(Map.Entry<Team, Initiative>::value)!!.key

    override fun getInitiative(team: Team): Initiative = initiativesByTeam[team]!!

    fun setInitiatives(initiativesByTeam: Map<Team, Initiative>): DefaultTurn = DefaultTurn(
        id = id,
        initiativesByTeam = initiativesByTeam
    )

    companion object {
        private val DEFAULT_INITIATIVES_BY_TEAM: Map<Team, Initiative> = defaultInitiatives()

        private fun defaultInitiatives(): Map<Team, Initiative> {
            val initiativesByTeam: MutableMap<Team, Initiative> = Team.values()
                .associate { it to Initiative.MIN }
                .toMutableMap()
            initiativesByTeam[Team.values().first()] = Initiative.MAX
            return initiativesByTeam
        }
    }
}
