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

package mechabellum.server.game.api.core.commands.general

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.grid.Grid
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.shouldBe
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object EndPhaseCommandSpec : Spek({
    describe("execute") {
        it("should end the phase") {
            // given: any phase is active
            val phase = mock<Phase>()

            // when: the command is executed
            val subject = EndPhaseCommand()
            subject.execute(phase)

            // then: it should end the phase
            Verify on phase that phase.end() was called
        }
    }
})

object GetGridCommandSpec : Spek({
    describe("execute") {
        it("should return the game grid") {
            // given: any phase is active with a configured grid
            val expected = mock<Grid>()
            val game = mock<Game> {
                on { grid } doReturn expected
            }
            val phase = mock<Phase> {
                on { this.game } doReturn game
            }

            // when: the command is executed
            val subject = GetGridCommand()
            val actual = subject.execute(phase)

            // then: it should return the configured grid
            actual shouldBe expected
        }
    }
})
