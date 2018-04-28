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

package mechabellum.server.game.api.core.commands

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.CommandContext
import mechabellum.server.game.api.core.CommandException
import mechabellum.server.game.api.core.features.GridFeature
import mechabellum.server.game.api.core.grid.Grid
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object GetGridCommandSpec : Spek({
    describe("execute") {
        it("it should return the game grid") {
            // given
            val expected = mock<Grid>()
            val gridFeature = mock<GridFeature> {
                on { grid } doReturn expected
            }
            val context = mock<CommandContext> {
                on { getFeature(GridFeature::class.java) } doReturn Option.some(gridFeature)
            }
            val subject = GetGridCommand()

            // when
            val actual = subject.execute(context)

            // then
            actual shouldBe expected
        }

        it("should throw exception when context does not provide required features") {
            // given
            val context = mock<CommandContext> {
                on { getFeature(GridFeature::class.java) } doReturn Option.none()
            }
            val subject = GetGridCommand()

            // when
            val func = { subject.execute(context) }

            // then
            val exceptionResult = func shouldThrow CommandException::class
            exceptionResult.exceptionMessage shouldContain GridFeature::class.java.simpleName
        }
    }
})
