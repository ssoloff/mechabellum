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

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.Turn
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team

internal class DefaultTurn(
    override val id: TurnId,
    private val initiativesByTeamHistory: List<Map<Team, Initiative>> = listOf(mapOf())
) : Turn {
    init {
        require(initiativesByTeamHistory.isNotEmpty()) { "expected initiative history to not be empty but was empty" }
    }

    val initiativeRollsIncomplete: Boolean
        get() = initiativesByTeamHistory.last().size != Team.values().size

    val initiativeWinner: Option<Team>
        get() = if (initiativeRollsIncomplete) {
            Option.none()
        } else {
            initiativesByTeamHistory.last().let {
                val maxInitiative = it.values.max()
                val teamsWithMaxInitiative = it.filterValues({ it == maxInitiative }).keys
                if (teamsWithMaxInitiative.size == 1) Option.some(teamsWithMaxInitiative.first()) else Option.none()
            }
        }

    val teamsWithoutInitiative: Collection<Team>
        get() = Team.values().toList() - initiativesByTeamHistory.last().keys

    override fun getInitiative(team: Team): Option<Initiative> = Option.of(initiativesByTeamHistory.last()[team])

    fun setInitiative(team: Team, initiative: Initiative): DefaultTurn {
        val newInitiativesByTeamHistory = initiativesByTeamHistory.toMutableList()
        val initiativesByTeam = newInitiativesByTeamHistory.last()
        if (initiativesByTeam.keys.containsAll(Team.values().toList())) {
            newInitiativesByTeamHistory.add(mapOf(team to initiative))
        } else {
            val newInitiativesByTeam = initiativesByTeam.toMutableMap()
            newInitiativesByTeam[team] = initiative
            newInitiativesByTeamHistory[newInitiativesByTeamHistory.lastIndex] = newInitiativesByTeam
        }

        return DefaultTurn(id = id, initiativesByTeamHistory = newInitiativesByTeamHistory)
    }
}
