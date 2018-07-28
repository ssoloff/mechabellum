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

package mechabellum.server.game.api.core.commands.movement

import mechabellum.server.game.api.core.StatelessCommand
import mechabellum.server.game.api.core.grid.Angle
import mechabellum.server.game.api.core.grid.Displacement
import mechabellum.server.game.api.core.phases.MovementPhase
import mechabellum.server.game.api.core.unit.Mech

/** Superclass for stateless commands that are executed during the movement phase. */
open class StatelessMovementCommand<R : Any>(
    action: (MovementPhase) -> R
) : StatelessCommand<MovementPhase, R>(MovementPhase::class, action)

/**
 * Command that moves the selected Mech to change its position.
 *
 * When executed, throws [IllegalArgumentException] if the selected Mech has insufficient movement points to move the
 * specified displacement.
 * When executed, throws [IllegalStateException] if a Mech is not selected.
 *
 * @param displacement The displacement by which the selected Mech will be moved.
 */
class MoveCommand(displacement: Displacement) : StatelessMovementCommand<Unit>({
    it.move(displacement)
})

/**
 * Command that selects the Mech to move during the movement phase.
 *
 * When executed, throws [IllegalArgumentException] if the Mech is not part of the game; if the Mech does not belong to
 * the team being moved; or if the Mech already has a movement selection for the current turn.
 * When executed, throws [IllegalStateException] if a Mech has already been selected for movement in the movement
 * phase.
 *
 * @param mech The Mech to manipulate during the movement phase.
 */
class SelectCommand(mech: Mech) : StatelessMovementCommand<Unit>({
    it.select(mech)
})

/**
 * Command that turns the selected Mech to change its facing.
 *
 * When executed, throws [IllegalArgumentException] if the selected Mech has insufficient movement points to turn the
 * specified angle.
 * When executed, throws [IllegalStateException] if a Mech is not selected.
 *
 * @param angle The angle by which the selected Mech will be turned.
 */
class TurnCommand(angle: Angle) : StatelessMovementCommand<Unit>({
    it.turn(angle)
})
