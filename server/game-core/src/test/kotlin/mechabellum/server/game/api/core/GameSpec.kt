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

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withCause
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class GameSpec(subjectFactory: () -> Game) : SubjectSpek<Game>({
    subject { subjectFactory() }

    describe("executeCommand") {
        it("should return command result when command succeeds") {
            // when
            val expectedResult = 42
            val actualResult = subject.executeCommand(object : Command<Int> {
                override fun execute(context: CommandContext): Int {
                    return expectedResult
                }
            })

            // then
            actualResult shouldEqual expectedResult
        }

        it("should throw wrapped exception when command fails with checked exception") {
            // given
            class FakeException : Exception()

            // when
            val operation = {
                subject.executeCommand(object : Command<Nothing> {
                    override fun execute(context: CommandContext): Nothing {
                        throw FakeException()
                    }
                })
            }

            // then
            operation shouldThrow GameException::class withCause FakeException::class
        }

        it("should throw original exception when command fails with unchecked exception") {
            // given
            class FakeRuntimeException : RuntimeException()

            // when
            val operation = {
                subject.executeCommand(object : Command<Nothing> {
                    override fun execute(context: CommandContext): Nothing {
                        throw FakeRuntimeException()
                    }
                })
            }

            // then
            operation shouldThrow FakeRuntimeException::class
        }
    }
})
