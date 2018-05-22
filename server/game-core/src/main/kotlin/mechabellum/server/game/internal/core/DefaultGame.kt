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
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.internal.core.grid.DefaultGrid
import mechabellum.server.game.internal.core.phases.DefaultInitializationPhase
import mechabellum.server.game.internal.core.unit.DefaultMech

internal class DefaultGame(specification: GameSpecification) : Game {
    override val grid: DefaultGrid = DefaultGrid(specification.gridSpecification)
    override var phase: DefaultPhase = DefaultInitializationPhase(this)
    val state: DefaultGameState = DefaultGameState()
}

internal class DefaultGameState {
    val mechRecordsById: MutableMap<MechId, MechRecord> = hashMapOf()
    var nextMechId: Int = 0

    fun getMechRecord(mechId: MechId): MechRecord =
        mechRecordsById[mechId] ?: throw IllegalArgumentException("unknown Mech ID ($mechId)")

    class MechRecord(
        val mech: DefaultMech,
        val position: Option<Position> = Option.none()
    ) {
        fun setPosition(position: Position): MechRecord = MechRecord(
            mech = mech,
            position = Option.some(position)
        )
    }
}
