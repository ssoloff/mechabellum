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

package mechabellum.server.game.api.core

import mechabellum.server.game.api.core.grid.Grid
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.mechanics.DieRoller
import mechabellum.server.game.api.core.mechanics.UniformDieRoller

/**
 * A specification for creating a new [Game].
 *
 * @property dieRoller The [DieRoller] used to generate die rolls for various events in the game.
 * @property gridSpecification The [GridSpecification] describing the [Grid] to create for the game.
 */
data class GameSpecification(val dieRoller: DieRoller = UniformDieRoller(), val gridSpecification: GridSpecification)
