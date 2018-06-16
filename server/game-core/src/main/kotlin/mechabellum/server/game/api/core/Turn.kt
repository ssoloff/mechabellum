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

/**
 * The unique identifier of a turn within a game.
 *
 * @property value The zero-based turn identifier.
 *
 * @throws IllegalArgumentException If [value] is negative.
 */
data class TurnId(val value: Int) {
    init {
        require(value >= 0) { "expected value to be non-negative but was $value" }
    }
}

/**
 * A game turn.
 *
 * Each turn encapsulates information about one instance of each of the following phases:
 *
 * - Initiative phase
 * - Movement phase per team
 * - Weapon attack phase per team
 * - End phase
 */
interface Turn {
    /** The turn identifier. */
    val id: TurnId
}
