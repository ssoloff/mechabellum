/*
 * Copyright (C) 2019 Mechabellum contributors
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
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech

/** The phase during which units from one team are deployed to the game grid. */
interface DeploymentPhase : Phase {
    /** The team that may deploy during this phase. */
    val team: Team

    /**
     * Deploys [mech] to the specified [position] with the specified [facing].
     *
     * @throws IllegalArgumentException If [mech] is not part of this game; if [mech] does not belong to the team being
     * deployed; or if [position] is outside the possible deployment positions for [mech].
     */
    fun deploy(mech: Mech, position: Position, facing: Direction)
}
