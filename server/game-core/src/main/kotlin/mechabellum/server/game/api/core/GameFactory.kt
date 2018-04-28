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

import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechSpecification

/**
 * Factory for creating [Game] implementations.
 *
 * Clients are expected to obtain an instance of this interface using the Java [java.util.ServiceLoader] framework.
 */
interface GameFactory {
    /** Returns a new [Game] for the specified [GameSpecification]. */
    fun newGame(specification: GameSpecification): Game

    /** Returns a new [Mech] for the specified [MechSpecification]. */
    fun newMech(specification: MechSpecification): Mech
}
