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
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech

/** The second phase within a turn in which all Mechs are moved. This phase is split into one sub-phase per team. */
interface MovementPhase : Phase {
    /** The team that may move during this phase. */
    val team: Team

    /**
     * Changes the facing of [mech] by turning it the specified [angle].
     *
     * @throws IllegalArgumentException If [mech] is not part of this game; or if [mech] does not belong to the team
     * being moved.
     */
    fun turn(mech: Mech, angle: Angle)
}
