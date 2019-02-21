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

package mechabellum.server.game.api.core

import mechabellum.server.game.api.core.grid.Grid

/** A BattleTech game. */
interface Game {
    /** The game grid. */
    val grid: Grid

    /** The active game phase. */
    val phase: Phase

    /**
     * The active game turn.
     *
     * @throws IllegalStateException If the first game turn has not yet begun.
     */
    val turn: Turn
}

/** A checked exception that indicates a recoverable error occurred within a game. */
class GameException(message: String) : Exception(message)
