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

package mechabellum.server.game.api.core.grid

/** A direction on a [Grid]. All directions are normal to a cell side. */
enum class Direction(private val normalizedAngleFromNorth: Angle) {
    NORTH(Angle.ZERO),
    NORTHEAST(Angle.ONE),
    SOUTHEAST(Angle.TWO),
    SOUTH(Angle.THREE),
    SOUTHWEST((-Angle.TWO).normalize()),
    NORTHWEST((-Angle.ONE).normalize());

    init {
        assert(normalizedAngleFromNorth.isNormalized())
    }

    /** The direction immediately clockwise (+1 sextant or +60 degrees) from this direction. */
    val clockwise: Direction
        get() = this + Angle.ONE

    /** The direction immediately counterclockwise (-1 sextant or -60 degrees) from this direction. */
    val counterclockwise: Direction
        get() = this - Angle.ONE

    /** The direction directly opposite (±3 sextants or ±180 degrees) from this direction. */
    val opposite: Direction
        get() = this + Angle.THREE

    /** Returns the direction relative to this direction after subtracting [angle]. */
    operator fun minus(angle: Angle): Direction = this + (-angle)

    /** Returns the direction relative to this direction after adding [angle]. */
    operator fun plus(angle: Angle): Direction {
        val newNormalizedAngleFromNorth = (normalizedAngleFromNorth + angle).normalize()
        return DIRECTIONS_ORDERED_BY_NORMALIZED_ANGLE_FROM_NORTH[newNormalizedAngleFromNorth.value]
    }

    companion object {
        private val DIRECTIONS_ORDERED_BY_NORMALIZED_ANGLE_FROM_NORTH =
            values().sortedBy(Direction::normalizedAngleFromNorth)
    }
}
