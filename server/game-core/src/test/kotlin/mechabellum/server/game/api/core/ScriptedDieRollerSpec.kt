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

import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

internal object ScriptedDieRollerSpec : SubjectSpek<ScriptedDieRoller>({
    subject { ScriptedDieRoller() }

    describe("addValues") {
        it("should throw exception if any value is less than 1") {
            // when: adding a sequence of scripted die roll values containing a value less than 1
            val operation = { subject.addValues(1, 0, 6) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "but at least one was out of range"
        }

        it("should throw exception if any value is greater than DIE_SIDES") {
            // when: adding a sequence of scripted die roll values containing a value greater than DIE_SIDES
            val operation = { subject.addValues(1, DieRoller.DIE_SIDES + 1, 6) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "but at least one was out of range"
        }
    }

    describe("roll") {
        it("should return scripted values") {
            // given: an ordered sequence of scripted die roll values
            subject.addValues(1, 5, 3)

            // when: rolling the die
            val roll1 = subject.roll()
            val roll2 = subject.roll()
            val roll3 = subject.roll()

            // then: it should return the scripted die roll values in the same order
            roll1 shouldEqual 1
            roll2 shouldEqual 5
            roll3 shouldEqual 3
        }

        it("should throw exception if no scripted value available") {
            // given: an exhausted sequence of scripted die roll values
            subject.addValues(1)
            subject.roll()

            // when: rolling the die
            val operation = { subject.roll() }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "expected at least one value to be present but was absent"
        }
    }
})
