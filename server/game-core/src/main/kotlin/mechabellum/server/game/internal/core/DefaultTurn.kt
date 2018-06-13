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
    private val initiativesByTeam: Map<Team, Initiative> = mapOf()
) : Turn {
    val initiativeWinner: Option<Team> = Option.of(initiativesByTeam.maxBy(Map.Entry<Team, Initiative>::value)?.key)

    override fun getInitiative(team: Team): Option<Initiative> = Option.of(initiativesByTeam[team])

    fun setInitiatives(initiativesByTeam: Map<Team, Initiative>): DefaultTurn = DefaultTurn(
        id = id,
        initiativesByTeam = initiativesByTeam
    )
}
