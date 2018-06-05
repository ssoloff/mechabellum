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
            // when
            val expectedResult = 42
            val actualResult = subject.executeCommand(StatelessCommand(Phase::class, { expectedResult }))

            // then
            actualResult shouldEqual expectedResult
        }

        it("should throw exception when command fails with game exception") {
            // when
            val exception = GameException("message")
            val operation = { subject.executeCommand(StatelessCommand(Phase::class) { throw exception }) }

            // then
            val exceptionResult = operation shouldThrow GameException::class
            exceptionResult.exception shouldBe exception
        }

        it("should throw exception when command fails with unexpected checked exception") {
            // given
            class FakeException : Exception()

            // when
            val operation = { subject.executeCommand(StatelessCommand(Phase::class) { throw FakeException() }) }

            // then
            operation shouldThrow UnexpectedCommandException::class withCause FakeException::class
        }

        it("should throw exception when command fails with unchecked exception") {
            // given
            class FakeRuntimeException : RuntimeException()

            // when
            val operation = { subject.executeCommand(StatelessCommand(Phase::class) { throw FakeRuntimeException() }) }

            // then
            operation shouldThrow FakeRuntimeException::class
        }

        it("should throw exception when command phase not active") {
            // given
            abstract class FakePhase : Phase

            // when
            val operation = { subject.executeCommand(StatelessCommand(FakePhase::class) {}) }

            // then
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "expected phase ${FakePhase::class.simpleName} to be active"
        }
    }
})

abstract class GameRunnerFactorySpec(newSubject: () -> GameRunnerFactory) : SubjectSpek<GameRunnerFactory>({
    subject { newSubject() }

    describe("newGameRunner") {
        it("should return a game runner with the requested grid specification") {
            // when
            val gridSpecification = newTestGridSpecification()
            val gameRunner = subject.newGameRunner(
                newTestGameSpecification().copy(gridSpecification = gridSpecification)
            )

            // then
            gameRunner.executeCommand(GetGridCommand()).type shouldEqual gridSpecification.type
        }
    }
})
