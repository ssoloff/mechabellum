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

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.common.api.core.util.Result
import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.grid.Grid
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object GetGridCommandSpec : Spek({
    describe("execute") {
        it("should return Success with the game grid") {
            // given
            val expected = mock<Grid>()
            val phase = mock<Phase> {
                on { grid } doReturn expected
            }
            val subject = GetGridCommand()

            // when
            val result = subject.execute(phase)

            // then
            result shouldEqual Result.success(expected)
        }
    }
})
