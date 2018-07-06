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

package mechabellum.server.game.api.core.unit

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team

/** The unique identifier of a Mech within a game. */
data class MechId(val value: Int)

/** A Mech. */
interface Mech {
    /**
     * The Mech facing or empty if the Mech currently has no explicit facing (e.g. it has not yet been deployed or it
     * has left the grid).
     */
    val facing: Option<Direction>

    /** The Mech identifier. */
    val id: MechId

    /**
     * The Mech position or empty if the Mech currently has no explicit position (e.g. it has not yet been deployed or
     * it has left the grid).
     */
    val position: Option<Position>

    /** The team to which the Mech belongs. */
    val team: Team
}
