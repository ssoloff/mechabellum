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

package mechabellum.server.game.api.core.commands.initialization

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.Verify
import org.amshove.kluent.any
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.shouldBe
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object EndInitializationCommandSpec : Spek({
    describe("execute") {
        it("should end the initialization phase") {
            // given
            val initializationPhase = mock<InitializationPhase>()
            val subject = EndInitializationCommand()

            // when
            subject.execute(initializationPhase)

            // then
            Verify on initializationPhase that initializationPhase.end() was called
        }
    }
})

object NewMechCommandSpec : Spek({
    describe("execute") {
        it("should return new Mech") {
            // given
            val expectedMech = mock<Mech>()
            val initializationPhase = mock<InitializationPhase> {
                on { newMech(any()) } doReturn expectedMech
            }
            val specification = newTestMechSpecification()
            val subject = NewMechCommand(specification)

            // when
            val actualMech = subject.execute(initializationPhase)

            // then
            Verify on initializationPhase that initializationPhase.newMech(specification) was called
            actualMech shouldBe expectedMech
        }
    }
})
