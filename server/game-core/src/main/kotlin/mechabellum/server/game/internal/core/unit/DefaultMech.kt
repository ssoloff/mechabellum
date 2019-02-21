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

package mechabellum.server.game.internal.core.unit

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId

internal class DefaultMech(
    override val facing: Option<Direction>,
    override val id: MechId,
    override val movementPoints: Int,
    override val position: Option<Position>,
    override val team: Team
) : Mech {
    init {
        assert(movementPoints >= 0) { "expected movement points to be non-negative but was $movementPoints" }
    }

    fun setFacing(facing: Direction): DefaultMech = DefaultMech(
        facing = Option.some(facing),
        id = id,
        movementPoints = movementPoints,
        position = position,
        team = team
    )

    fun setMovementPoints(movementPoints: Int): DefaultMech = DefaultMech(
        facing = facing,
        id = id,
        movementPoints = movementPoints,
        position = position,
        team = team
    )

    fun setPosition(position: Position): DefaultMech = DefaultMech(
        facing = facing,
        id = id,
        movementPoints = movementPoints,
        position = Option.some(position),
        team = team
    )
}
