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

package mechabellum.server.game.core.commands

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.common.core.util.Option
import mechabellum.server.game.core.CommandContext
import mechabellum.server.game.core.CommandException
import mechabellum.server.game.core.features.DeploymentFeature
import mechabellum.server.game.core.grid.CellId
import mechabellum.server.game.core.unit.Mech
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DeployMechCommandSpec : Spek({
    describe("execute") {
        it("should deploy mech to specified position") {
            // given
            val deploymentFeature = mock<DeploymentFeature>()
            val context = mock<CommandContext> {
                on { getFeature(DeploymentFeature::class.java) } doReturn Option.some(deploymentFeature)
            }
            val mech = mock<Mech>()
            val position = CellId(3, 6)
            val subject = DeployMechCommand(mech, position)

            // when
            subject.execute(context)

            // then
            Verify on deploymentFeature that deploymentFeature.deployMech(mech, position) was called
        }

        it("should throw exception when context does not provide required features") {
            // given
            val context = mock<CommandContext> {
                on { getFeature(DeploymentFeature::class.java) } doReturn Option.none()
            }
            val subject = DeployMechCommand(mock(), CellId(3, 6))

            // when
            val func = { subject.execute(context) }

            // then
            val exceptionResult = func shouldThrow CommandException::class
            exceptionResult.exceptionMessage shouldContain DeploymentFeature::class.java.simpleName
        }
    }
})
