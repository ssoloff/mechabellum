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

import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import kotlin.reflect.KClass

abstract class CommandContextSpec(
    activePhaseType: KClass<out Phase>,
    subjectFactory: () -> CommandContext
) : SubjectSpek<CommandContext>({
    subject { subjectFactory() }

    describe("phase") {
        it("should return active phase") {
            subject.phase shouldBeInstanceOf activePhaseType
        }
    }

    describe("getPhaseAs") {
        it("should return phase when active phase is of specified type") {
            subject.getPhaseAs(activePhaseType) shouldBeInstanceOf activePhaseType
        }

        it("should throw exception when active phase is not of specified type") {
            // given
            abstract class DummyPhase : Phase

            // when
            val func = { subject.getPhaseAs(DummyPhase::class) }

            // then
            val exceptionResult = func shouldThrow CommandException::class
            exceptionResult.exceptionMessage shouldContain DummyPhase::class.java.simpleName
        }
    }
})
