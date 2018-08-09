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

import mechabellum.server.common.api.core.util.getOrThrow
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.grid.Angle
import mechabellum.server.game.api.core.grid.Displacement
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.MovementPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultTurnPhase
import mechabellum.server.game.internal.core.mechanics.MovementSelection

internal class DefaultMovementPhase(
    game: DefaultGame,
    override val team: Team,
    turnId: TurnId
) : DefaultTurnPhase(game, turnId), MovementPhase {
    override fun end() {
        checkMovementSelectionIsActive()

        game.phase = if (haveAllMechsMoved()) {
            DefaultWeaponAttackPhase(
                game = game,
                team = turn.initiativeHistory.initiativeWinner.getOrThrow().opponent,
                turnId = turnId
            )
        } else {
            DefaultMovementPhase(game = game, team = team.opponent, turnId = turnId)
        }
        game.state.modifyTurn(turnId) { turn -> turn.endMovementSelection() }
    }

    private fun checkMovementSelectionIsActive(): MovementSelection = turn.movementSelection.getOrThrow {
        IllegalStateException("no active movement selection")
    }

    private fun haveAllMechsMoved(): Boolean = turn.movementSelections.size == game.state.mechs.size

    override fun move(displacement: Displacement) {
        val movementSelection = checkMovementSelectionIsActive()

        game.state.modifyMech(movementSelection.mechId) { mech ->
            checkMechHasSufficientMovementPointsForMove(mech, displacement)
            val newPosition = mech.position.getOrThrow() + displacement
            checkPositionIsWithinGridBounds(newPosition)

            mech
                .setPosition(newPosition)
                .setMovementPoints(mech.movementPoints - displacement.magnitude)
        }
    }

    private fun checkMechHasSufficientMovementPointsForMove(mech: Mech, displacement: Displacement) =
        require(mech.movementPoints >= displacement.magnitude) {
            "expected Mech to have at least ${displacement.magnitude} movement points but was ${mech.movementPoints}"
        }

    private fun checkPositionIsWithinGridBounds(position: Position) = require(position in game.grid.positions) {
        "expected final position of Mech to be within grid bounds but was outside grid bounds ($position)"
    }

    override fun select(mech: Mech) {
        checkMechExists(mech)
        checkMechBelongsToMovingTeam(mech)
        checkMechHasNotAlreadyMoved(mech)

        game.state.modifyTurn(turnId) { turn -> turn.beginMovementSelection(MovementSelection(mech.id)) }
    }

    private fun checkMechExists(mech: Mech) {
        game.state.getMech(mech.id)
    }

    private fun checkMechBelongsToMovingTeam(mech: Mech) = require(mech.team == team) {
        "expected team $team to be moved during this phase but was ${mech.team}"
    }

    private fun checkMechHasNotAlreadyMoved(mech: Mech) =
        require(turn.movementSelections.none { it.mechId == mech.id }) {
            "Mech with ID ${mech.id} has already moved during the current turn"
        }

    override fun turn(angle: Angle) {
        val movementSelection = checkMovementSelectionIsActive()

        game.state.modifyMech(movementSelection.mechId) { mech ->
            checkMechHasSufficientMovementPointsForTurn(mech, angle)

            mech
                .setFacing(mech.facing.getOrThrow() + angle)
                .setMovementPoints(mech.movementPoints - angle.value)
        }
    }

    private fun checkMechHasSufficientMovementPointsForTurn(mech: Mech, angle: Angle) =
        require(mech.movementPoints >= angle.value) {
            "expected Mech to have at least ${angle.value} movement points but was ${mech.movementPoints}"
        }
}
