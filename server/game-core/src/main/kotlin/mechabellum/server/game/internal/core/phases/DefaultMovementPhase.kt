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

import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.grid.Angle
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.participant.Teamable
import mechabellum.server.game.api.core.phases.MovementPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultTurnPhase
import mechabellum.server.game.internal.core.participant.DefaultTeamable

internal class DefaultMovementPhase(
    game: DefaultGame,
    team: Team,
    turnId: TurnId
) : DefaultTurnPhase(game, turnId), MovementPhase, Teamable by DefaultTeamable(team) {
    override fun end() {
        TODO("not implemented")
    }

    override fun turn(mech: Mech, angle: Angle) {
        checkMechBelongsToMovingTeam(mech)

        game.state.modifyMechRecord(mech.id) { it.setFacing(it.facing.getOrThrow() + angle) }
    }

    private fun checkMechBelongsToMovingTeam(mech: Mech) {
        require(mech.team == team) { "expected team $team to be moved during this phase but was ${mech.team}" }
    }
}
