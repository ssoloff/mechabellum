// ktlint-disable filename
// (only necessary until a second command spec is added to this file)

/*
 * Copyright (C) 2019 Mechabellum contributors
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

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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

object NewMechCommandSpec : Spek({
    describe("execute") {
        it("should return new Mech") {
            // given: the initialization phase is active
            val expectedMech = mock<Mech>()
            val initializationPhase = mock<InitializationPhase> {
                on { newMech(any()) } doReturn expectedMech
            }

            // when: the command is executed
            val specification = newTestMechSpecification()
            val subject = NewMechCommand(specification)
            val actualMech = subject.execute(initializationPhase)

            // then: it should return a new Mech
            Verify on initializationPhase that initializationPhase.newMech(specification) was called
            actualMech shouldBe expectedMech
        }
    }
})
