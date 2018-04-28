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

import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameFactory
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.grid.InternalGrid
import mechabellum.server.game.internal.core.unit.InternalMech

internal class InternalGameFactory : GameFactory {
    private var _nextMechId: Int = 0

    override fun newGame(specification: GameSpecification): Game = InternalGame(
        grid = InternalGrid(specification.gridSpecification.type)
    )

    override fun newMech(specification: MechSpecification): Mech = InternalMech(MechId(_nextMechId++))
}
