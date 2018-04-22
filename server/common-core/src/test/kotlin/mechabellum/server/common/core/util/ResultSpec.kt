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

package mechabellum.server.common.core.util

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

typealias SpecResult = Result<Number, RuntimeException>

object ResultSpec : Spek({
    describe("Empty") {
        val subject: SpecResult = Result.empty()

        describe("getOrElse") {
            it("should return default value") {
                subject.getOrElse(2112) shouldEqual 2112
            }
        }

        describe("getOrThrow") {
            it("should throw exception") {
                ({ subject.getOrThrow() }) shouldThrow NoSuchElementException::class
            }
        }

        describe("toString") {
            it("should return Empty") {
                subject.toString() shouldEqual "Empty"
            }
        }

        describe("when") {
            it("should take Empty branch when target is Empty type") {
                when (subject) {
                    is Result.Empty -> Unit
                    else -> throw AssertionError("expected Empty but was $subject")
                }
            }

            it("should take Empty branch when target is Empty instance") {
                when (subject) {
                    Result.empty<Number, RuntimeException>() -> Unit
                    else -> throw AssertionError("expected Empty but was $subject")
                }
            }
        }
    }

    describe("Failure") {
        val exception = NumberFormatException("message")
        val otherException = IllegalStateException("otherMessage")
        val subject: SpecResult = Result.failure(exception)

        describe("getOrElse") {
            it("should return default value") {
                subject.getOrElse(2112) shouldEqual 2112
            }
        }

        describe("getOrThrow") {
            it("should throw exception") {
                ({ subject.getOrThrow() }) shouldThrow exception
            }
        }

        describe("toString") {
            it("should return Failure(exception.message)") {
                subject.toString() shouldEqual "Failure(${exception.message})"
            }
        }

        describe("when") {
            it("should take Failure branch when target is Failure type") {
                when (subject) {
                    is Result.Failure -> subject.exception shouldEqual exception
                    else -> throw AssertionError("expected Failure but was $subject")
                }
            }

            it("should take Failure branch when target is equal Failure instance") {
                val result: SpecResult = Result.failure(exception)
                when (subject) {
                    result -> Unit
                    else -> throw AssertionError("expected $result but was $subject")
                }
            }

            it("should take else branch when target is unequal Failure instance") {
                val result: SpecResult = Result.failure(otherException)
                when (subject) {
                    result -> throw AssertionError("expected $subject but was $result")
                    else -> Unit
                }
            }
        }
    }

    describe("Success") {
        val value = 42
        val otherValue = 2112.0
        val subject: SpecResult = Result.success(value)

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
            it("should return Success(value)") {
                subject.toString() shouldEqual "Success($value)"
            }
        }

        describe("when") {
            it("should take Success branch when target is Success type") {
                when (subject) {
                    is Result.Success -> subject.value shouldEqual value
                    else -> throw AssertionError("expected Success but was $subject")
                }
            }

            it("should take Success branch when target is equal Success instance") {
                val result: SpecResult = Result.success(value)
                when (subject) {
                    result -> Unit
                    else -> throw AssertionError("expected $result but was $subject")
                }
            }

            it("should take else branch when target is unequal Success instance") {
                val result: SpecResult = Result.success(otherValue)
                when (subject) {
                    result -> throw AssertionError("expected $subject but was $result")
                    else -> Unit
                }
            }
        }
    }
})
