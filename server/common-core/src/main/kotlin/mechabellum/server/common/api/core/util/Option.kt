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

package mechabellum.server.common.api.core.util

/** A container for a value that may or may not be present. */
sealed class Option<out T : Any> {
    /** Returns the value if present; otherwise throws [NoSuchElementException]. */
    abstract fun getOrThrow(): T

    companion object {
        private val NONE = None()

        /** Returns the empty container. */
        fun <T : Any> none(): Option<T> =
            NONE

        /** Returns a new container containing [value] if it is not null; otherwise returns the empty container. */
        fun <T : Any> of(value: T?): Option<T> = if (value != null) some(value) else none()

        /** Returns a new container containing [value]. */
        fun <T : Any> some(value: T): Option<T> = Some(value)
    }

    /** Option that represents the absence of a value. */
    class None internal constructor() : Option<Nothing>() {
        override fun getOrThrow(): Nothing = throw NoSuchElementException("no value present")

        override fun toString(): String = "None"
    }

    /** Option that represents the presence of [value]. */
    data class Some<out T : Any> internal constructor(val value: T) : Option<T>() {
        override fun getOrThrow(): T = value

        override fun toString(): String = "Some($value)"
    }
}

/** Returns the value if present; otherwise returns [defaultValue]. */
fun <T : Any> Option<T>.getOrElse(defaultValue: T): T = when (this) {
    is Option.Some -> value
    else -> defaultValue
}
