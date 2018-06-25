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

package mechabellum.server.game.internal.core.mechanics

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team

internal typealias InitiativeIteration = Map<Team, Initiative>

internal class InitiativeHistory(private val iterations: List<InitiativeIteration> = listOf(mapOf())) {
    init {
        require(iterations.isNotEmpty()) { "expected iterations to not be empty but was empty" }
    }

    private val currentIteration: InitiativeIteration
        get() = iterations.last()

    val initiativeRollsComplete: Boolean
        get() = currentIteration.size == Team.values().size

    val initiativeWinner: Option<Team>
        get() = if (initiativeRollsComplete) {
            val maxInitiative = currentIteration.values.max()
            val teamsWithMaxInitiative = currentIteration.filterValues { it == maxInitiative }.keys
            if (teamsWithMaxInitiative.size == 1) Option.some(teamsWithMaxInitiative.first()) else Option.none()
        } else {
            Option.none()
        }

    val teamsWithoutInitiative: Collection<Team>
        get() = Team.values().toList() - currentIteration.keys

    fun getInitiative(team: Team): Option<Initiative> = Option.of(currentIteration[team])

    fun setInitiative(team: Team, initiative: Initiative): InitiativeHistory {
        val newIterations = iterations.toMutableList()
        val iteration = newIterations.last()
        if (iteration.keys.containsAll(Team.values().toList())) {
            newIterations.add(mapOf(team to initiative))
        } else {
            val newIteration = iteration.toMutableMap()
            newIteration[team] = initiative
            newIterations[newIterations.lastIndex] = newIteration
        }

        return InitiativeHistory(newIterations)
    }
}
