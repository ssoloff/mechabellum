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

package mechabellum.server.game.api.core.mechanics

/** An initiative result. */
data class Initiative(val value: Int) : Comparable<Initiative> {
    init {
        require(value in MIN_VALUE..MAX_VALUE) {
            "expected value in range [$MIN_VALUE,$MAX_VALUE] but was $value"
        }
    }

    override fun compareTo(other: Initiative): Int = COMPARATOR.compare(this, other)

    companion object {
        private val COMPARATOR = Comparator.comparingInt(Initiative::value)

        private const val MAX_VALUE: Int = 12

        /** The maximum initiative result. */
        val MAX: Initiative = Initiative(MAX_VALUE)

        private const val MIN_VALUE: Int = 2

        /** The minimum initiative result. */
        val MIN: Initiative = Initiative(MIN_VALUE)
    }
}
