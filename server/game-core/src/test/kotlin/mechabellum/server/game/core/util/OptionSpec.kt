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

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object OptionSpec : Spek({
    describe("None") {
        val subject: Option<Number> = Option.none()

        describe("getOrElse") {
            it("should return default value") {
                subject.getOrElse(2112) shouldEqual 2112
            }
        }

        describe("getOrThrow") {
            it("should throw exception") {
                ({ subject.getOrThrow() }) shouldThrow IllegalStateException::class
            }
        }

        describe("toString") {
            it("should return None") {
                subject.toString() shouldEqual "None"
            }
        }

        describe("when") {
            it("should take None branch when target is None type") {
                when (subject) {
                    is Option.None -> Unit
                    else -> throw AssertionError("expected None but was $subject")
                }
            }

            it("should take None branch when target is None instance") {
                when (subject) {
                    Option.none<Number>() -> Unit
                    else -> throw AssertionError("expected None but was $subject")
                }
            }
        }
    }

    describe("Some") {
        val value = 42
        val otherValue = 2112.0
        val subject: Option<Number> = Option.some(value)

        describe("getOrElse") {
            it("should return value") {
                subject.getOrElse(otherValue) shouldEqual value
            }
        }

        describe("getOrThrow") {
            it("should return value") {
                subject.getOrThrow() shouldEqual value
            }
        }

        describe("toString") {
            it("should return Some(value)") {
                subject.toString() shouldEqual "Some($value)"
            }
        }

        describe("when") {
            it("should take Some branch when target is Some type") {
                when (subject) {
                    is Option.Some -> subject.value shouldEqual value
                    else -> throw AssertionError("expected Some but was $subject")
                }
            }

            it("should take Some branch when target is equal Some instance") {
                when (subject) {
                    Option.some(value) -> Unit
                    else -> throw AssertionError("expected Some($value) but was $subject")
                }
            }

            it("should take else branch when target is unequal Some instance") {
                when (subject) {
                    Option.some(otherValue) -> throw AssertionError("expected $subject but was Some($otherValue)")
                    else -> Unit
                }
            }
        }
    }
})
