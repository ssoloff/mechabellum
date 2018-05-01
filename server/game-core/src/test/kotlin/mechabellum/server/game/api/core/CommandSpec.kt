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

import mechabellum.server.common.api.core.util.Option
import org.amshove.kluent.shouldBeInstanceOf
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class CommandContextSpec(
    activePhaseType: Class<out Phase>,
    subjectFactory: () -> CommandContext
) : SubjectSpek<CommandContext>({
    subject { subjectFactory() }

    describe("getActivePhase") {
        it("should return active phase") {
            subject.getActivePhase() shouldBeInstanceOf activePhaseType
        }
    }

    describe("getActivePhaseAs") {
        it("should return Some when active phase is of specified type") {
            subject.getActivePhaseAs(activePhaseType) shouldBeInstanceOf Option.Some::class
        }

        it("should return None when active phase is not of specified type") {
            abstract class DummyPhase : Phase
            subject.getActivePhaseAs(DummyPhase::class.java) shouldBeInstanceOf Option.None::class
        }
    }
})
