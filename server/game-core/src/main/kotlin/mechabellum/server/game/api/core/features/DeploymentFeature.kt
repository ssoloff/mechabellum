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

package mechabellum.server.game.api.core.features

import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechSpecification

/** A game feature that provides behavior to deploy units in the game. */
interface DeploymentFeature {
    /**
     * Creates a new Mech based on [specification], deploys it to [position] on the game grid, and returns the deployed
     * Mech.
     *
     * @throws IllegalArgumentException If [position] does not exist on the game grid.
     */
    fun deployMech(specification: MechSpecification, position: CellId): Mech
}
