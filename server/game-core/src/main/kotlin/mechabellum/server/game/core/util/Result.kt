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

package mechabellum.server.game.core.util

/**
 * A container for the result of a computation that may complete successfully and provide a value (success), complete
 * successfully and not provide a value (empty), or fail with an exception (failure).
 */
sealed class Result<out T : Any, out E : Exception> {
    /**
     * Returns the value if [Success], throws the exception if [Failure], or throws [NoSuchElementException] if
     * [Empty].
     */
    abstract fun getOrThrow(): T

    companion object {
        private val EMPTY = Empty()

        /** Returns the empty result. */
        fun <T : Any, E : Exception> empty(): Result<T, E> = EMPTY

        /** Returns a new failure result containing [exception]. */
        fun <T : Any, E : Exception> failure(exception: E): Result<T, E> = Failure(exception)

        /** Returns a new success result containing [value]. */
        fun <T : Any, E : Exception> success(value: T): Result<T, E> = Success(value)
    }

    /** Result that represents success with no value. */
    class Empty internal constructor() : Result<Nothing, Nothing>() {
        override fun getOrThrow(): Nothing = throw NoSuchElementException("no value or exception present")

        override fun toString(): String = "Empty"
    }

    /** Result that represents failure with [exception]. */
    data class Failure<out E : Exception> internal constructor(val exception: E) : Result<Nothing, E>() {
        override fun getOrThrow(): Nothing = throw exception

        override fun toString(): String = "Failure(${exception.message})"
    }

    /** Result that represents success with [value]. */
    data class Success<out T : Any> internal constructor(val value: T) : Result<T, Nothing>() {
        override fun getOrThrow(): T = value

        override fun toString(): String = "Success($value)"
    }
}

/** Returns the value if [Result.Success]; otherwise returns [defaultValue]. */
fun <T : Any, E : Exception> Result<T, E>.getOrElse(defaultValue: T): T = when (this) {
    is Result.Success -> value
    else -> defaultValue
}
