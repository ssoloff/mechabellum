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

import mechabellum.server.game.api.core.commands.general.GetGridCommand
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withCause
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class GameRunnerSpec(newSubject: () -> GameRunner) : SubjectSpek<GameRunner>({
    subject { newSubject() }

    describe("executeCommand") {
        it("should return command result when command succeeds") {
            // when: executing a command that completes successfully
            val expectedResult = 42
            val actualResult = subject.executeCommand(StatelessCommand(Phase::class, { expectedResult }))

            // then: it should return the command result
            actualResult shouldEqual expectedResult
        }

        it("should throw exception when command fails with game exception") {
            // when: executing a command that throws GameException
            val exception = GameException("message")
            val operation = { subject.executeCommand(StatelessCommand(Phase::class) { throw exception }) }

            // then: it should throw the original exception
            val exceptionResult = operation shouldThrow GameException::class
            exceptionResult.exception shouldBe exception
        }

        it("should throw exception when command fails with unexpected checked exception") {
            // given: a custom checked exception
            class FakeException : Exception()

            // when: executing a command that throws a checked exception
            val operation = { subject.executeCommand(StatelessCommand(Phase::class) { throw FakeException() }) }

            // then: it should throw an UnexpectedCommandException with the original exception as the cause
            operation shouldThrow UnexpectedCommandException::class withCause FakeException::class
        }

        it("should throw exception when command fails with unchecked exception") {
            // given: a custom unchecked exception
            class FakeRuntimeException : RuntimeException()

            // when: executing a command that throws an unchecked exception
            val operation = { subject.executeCommand(StatelessCommand(Phase::class) { throw FakeRuntimeException() }) }

            // then: it should throw the original exception
            operation shouldThrow FakeRuntimeException::class
        }

        it("should throw exception when command phase not active") {
            // given: a custom phase
            abstract class FakePhase : Phase

            // when: executing a command during the wrong phase
            val operation = { subject.executeCommand(StatelessCommand(FakePhase::class) {}) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "expected phase ${FakePhase::class.simpleName} to be active"
        }
    }
})

abstract class GameRunnerFactorySpec(newSubject: () -> GameRunnerFactory) : SubjectSpek<GameRunnerFactory>({
    subject { newSubject() }

    describe("newGameRunner") {
        it("should return a game runner with the requested game specification") {
            // when: creating a new game runner
            val gridSpecification = newTestGridSpecification()
            val gameRunner = subject.newGameRunner(
                newTestGameSpecification().copy(gridSpecification = gridSpecification)
            )

            // then: it should be configured using the specified game specification
            gameRunner.executeCommand(GetGridCommand()).type shouldEqual gridSpecification.type
        }
    }
})
