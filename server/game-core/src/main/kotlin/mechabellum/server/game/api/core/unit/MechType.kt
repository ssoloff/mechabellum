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

package mechabellum.server.game.api.core.unit

/**
 * The data describing a type of [Mech].
 *
 * @property name The unique name for this type of Mech (e.g. "HBK-4G Hunchback").
 * @property walkingMovementPoints The number of movement points available to Mechs of this type when walking.
 */
data class MechType(val name: String, val walkingMovementPoints: Int) {
    init {
        require(walkingMovementPoints > 0) { "expected walking movement points to be positive but was $walkingMovementPoints" }
    }
}
