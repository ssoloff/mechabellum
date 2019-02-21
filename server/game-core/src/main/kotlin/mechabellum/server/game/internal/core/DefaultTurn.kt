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

package mechabellum.server.game.internal.core

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.Turn
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.internal.core.mechanics.InitiativeHistory
import mechabellum.server.game.internal.core.mechanics.MovementSelection

internal class DefaultTurn(
    override val id: TurnId,
    val initiativeHistory: InitiativeHistory = InitiativeHistory(),
    val movementSelection: Option<MovementSelection> = Option.none(),
    val movementSelections: Collection<MovementSelection> = emptyList()
) : Turn {
    init {
        val mechIds: MutableSet<MechId> = mutableSetOf()
        assert(movementSelections.all { mechIds.add(it.mechId) }) {
            "expected one movement selection per Mech but was $movementSelections"
        }
    }

    fun beginMovementSelection(movementSelection: MovementSelection): DefaultTurn {
        checkCanBeginMovementSelection(movementSelection)

        return DefaultTurn(
            id = id,
            initiativeHistory = initiativeHistory,
            movementSelection = Option.some(movementSelection),
            movementSelections = movementSelections + movementSelection
        )
    }

    private fun checkCanBeginMovementSelection(movementSelection: MovementSelection) {
        if (this.movementSelection is Option.Some) {
            throw IllegalStateException(
                "cannot begin movement selection for Mech with ID ${movementSelection.mechId} " +
                    "when movement selection active for Mech with ID ${this.movementSelection.value.mechId}"
            )
        }
    }

    fun endMovementSelection(): DefaultTurn {
        checkCanEndMovementSelection()

        return DefaultTurn(
            id = id,
            initiativeHistory = initiativeHistory,
            movementSelection = Option.none(),
            movementSelections = movementSelections
        )
    }

    private fun checkCanEndMovementSelection() {
        check(movementSelection is Option.Some) { "cannot end movement selection when no active movement selection" }
    }

    fun setInitiativeHistory(initiativeHistory: InitiativeHistory): DefaultTurn = DefaultTurn(
        id = id,
        initiativeHistory = initiativeHistory,
        movementSelection = movementSelection,
        movementSelections = movementSelections
    )
}
