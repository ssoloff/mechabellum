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

package mechabellum.server.game.api.core.phases

import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.grid.Angle
import mechabellum.server.game.api.core.grid.Displacement
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech

/** The second phase within a turn in which all Mechs are moved. This phase is split into one sub-phase per Mech. */
interface MovementPhase : Phase {
    /** The team that may move during this phase. */
    val team: Team

    /**
     * @throws IllegalStateException If a Mech is not selected.
     */
    override fun end()

    /**
     * Changes the position of the selected Mech by moving it the specified [displacement].
     *
     * @throws IllegalArgumentException If the selected Mech has insufficient movement points to move the specified
     * [displacement]; or if the final position of the selected Mech would be outside the grid bounds.
     * @throws IllegalStateException If a Mech is not selected.
     */
    fun move(displacement: Displacement)

    /**
     * Selects the [mech] to move during this phase.
     *
     * This method begins a movement selection for [mech], which is completed when ending the phase. This method must
     * be the first method called on this interface during the movement phase.
     *
     * @throws IllegalArgumentException If [mech] is not part of this game; if [mech] does not belong to the team being
     * moved; or if [mech] already has a movement selection for the current turn.
     * @throws IllegalStateException If a Mech has already been selected for movement in this phase.
     */
    fun select(mech: Mech)

    /**
     * Changes the facing of the selected Mech by turning it the specified [angle].
     *
     * @throws IllegalArgumentException If the selected Mech has insufficient movement points to turn the specified
     * [angle].
     * @throws IllegalStateException If a Mech is not selected.
     */
    fun turn(angle: Angle)
}
