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
import mechabellum.server.game.api.core.mechanics.DieRoller
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.internal.core.grid.DefaultGrid
import mechabellum.server.game.internal.core.phases.DefaultInitializationPhase
import mechabellum.server.game.internal.core.unit.DefaultMech

internal class DefaultGame(specification: GameSpecification) : Game {
    val dieRoller: DieRoller = specification.dieRoller
    override val grid: DefaultGrid = DefaultGrid(specification.gridSpecification)
    override var phase: DefaultPhase = DefaultInitializationPhase(this)
    val state: DefaultGameState = DefaultGameState()

    override val turn: DefaultTurn
        get() = state.turn
}

internal class DefaultGameState {
    private val mechRecordsById: MutableMap<MechId, MechRecord> = hashMapOf()
    private var nextMechIdValue: Int = 0
    private val turns: MutableList<DefaultTurn> = mutableListOf()

    val mechRecords: Collection<MechRecord>
        get() = mechRecordsById.values

    val turn: DefaultTurn
        get() = turns.lastOrNull()
            ?: throw IllegalStateException("expected at least one turn to be present but was absent")

    fun addMechRecord(mechRecord: MechRecord) {
        val mechId = mechRecord.mech.id
        require(mechId !in mechRecordsById) { "expected Mech record with ID $mechId to be absent but was present" }
        mechRecordsById[mechId] = mechRecord
    }

    fun addTurn(): TurnId {
        val turnId = TurnId(turns.size)
        turns.add(DefaultTurn(id = turnId))
        return turnId
    }

    fun getMechRecord(mechId: MechId): MechRecord = mechRecordsById.getOrElse(mechId) {
        throw IllegalArgumentException("expected Mech record with ID $mechId to be present but was absent")
    }

    fun getTurn(turnId: TurnId): DefaultTurn = turns.getOrElse(turnId.value) {
        throw IllegalArgumentException("expected turn with ID $turnId to be present but was absent")
    }

    fun modifyMechRecord(mechId: MechId, action: (MechRecord) -> MechRecord) {
        mechRecordsById[mechId] = action(getMechRecord(mechId))
    }

    fun modifyTurn(turnId: TurnId, action: (DefaultTurn) -> DefaultTurn) {
        turns[turnId.value] = action(getTurn(turnId))
    }

    fun newMechId(): MechId = MechId(nextMechIdValue++)

    class MechRecord(
        val mech: DefaultMech,
        val facing: Option<Direction> = Option.none(),
        val position: Option<Position> = Option.none()
    ) {
        fun setFacing(facing: Direction): MechRecord = MechRecord(
            facing = Option.some(facing),
            mech = mech,
            position = position
        )

        fun setPosition(position: Position): MechRecord = MechRecord(
            facing = facing,
            mech = mech,
            position = Option.some(position)
        )
    }
}
