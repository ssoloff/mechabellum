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
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.MovementPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultTurnPhase

internal class DefaultMovementPhase(
    game: DefaultGame,
    override val team: Team,
    turnId: TurnId
) : DefaultTurnPhase(game, turnId), MovementPhase {
    override fun end() {
        TODO("not implemented")
    }

    override fun move(mech: Mech, magnitude: Int, direction: Direction) {
        checkMechBelongsToMovingTeam(mech)

        game.state.modifyMech(mech.id) {
            val normalizedMagnitude = if (magnitude >= 0) magnitude else -magnitude
            val normalizedDirection = if (magnitude >= 0) direction else direction.opposite
            var displacementsRemaining = normalizedMagnitude
            var newPosition = it.position.getOrThrow()
            while (displacementsRemaining-- > 0) {
                val isOddColumn = (newPosition.col and 1) != 0
                newPosition = when (normalizedDirection) {
                    Direction.NORTH -> Position(newPosition.col, newPosition.row - 1)
                    Direction.NORTHEAST -> Position(newPosition.col + 1, newPosition.row - if (isOddColumn) 0 else 1)
                    Direction.SOUTHEAST -> Position(newPosition.col + 1, newPosition.row + if (isOddColumn) 1 else 0)
                    Direction.SOUTH -> Position(newPosition.col, newPosition.row + 1)
                    Direction.SOUTHWEST -> Position(newPosition.col - 1, newPosition.row + if (isOddColumn) 1 else 0)
                    Direction.NORTHWEST -> Position(newPosition.col - 1, newPosition.row - if (isOddColumn) 0 else 1)
                }
            }
            it.setPosition(newPosition)
        }
    }

    private fun checkMechBelongsToMovingTeam(mech: Mech) {
        require(mech.team == team) { "expected team $team to be moved during this phase but was ${mech.team}" }
    }

    override fun turn(mech: Mech, angle: Angle) {
        checkMechBelongsToMovingTeam(mech)

        game.state.modifyMech(mech.id) { it.setFacing(it.facing.getOrThrow() + angle) }
    }
}
