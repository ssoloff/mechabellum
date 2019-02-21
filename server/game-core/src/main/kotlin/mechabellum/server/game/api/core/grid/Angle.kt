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

package mechabellum.server.game.api.core.grid

/**
 * An angle between two [Direction]s.
 *
 * @property value The angle in sextants (1 sextant equals 60 degrees). A positive value represents a
 * clockwise rotation, while a negative value represents a counterclockwise rotation (due to the grid using a
 * left-handed coordinate system).
 */
data class Angle(val value: Int) : Comparable<Angle> {
    override fun compareTo(other: Angle): Int = COMPARATOR.compare(this, other)

    /** Returns `true` if this angle is normalized; otherwise `false`. */
    fun isNormalized(): Boolean = value in 0 until Cell.SIDE_COUNT

    /** Returns the difference of subtracting [other] from this angle. */
    operator fun minus(other: Angle): Angle = Angle(value - other.value)

    /** Returns this angle normalized in the range [0,6) sextants. */
    fun normalize(): Angle = Angle(Math.floorMod(value, Cell.SIDE_COUNT))

    /** Returns the sum of adding [other] to this angle. */
    operator fun plus(other: Angle): Angle = Angle(value + other.value)

    /** Returns the negative of this angle. */
    operator fun unaryMinus(): Angle = Angle(-value)

    companion object {
        private val COMPARATOR = Comparator.comparingInt(Angle::value)

        /** The constant angle of +1 sextant. */
        val ONE = Angle(1)

        /** The constant angle of +3 sextants. */
        val THREE = Angle(3)

        /** The constant angle of +2 sextants. */
        val TWO = Angle(2)

        /** The constant angle of 0 sextants. */
        val ZERO = Angle(0)
    }
}
