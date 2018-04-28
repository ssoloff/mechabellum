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
import mechabellum.server.game.api.core.features.DeploymentFeature
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.Verify
import org.amshove.kluent.any
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DeployMechCommandSpec : Spek({
    describe("execute") {
        it("should deploy Mech to specified position and return new Mech") {
            // given
            val expectedMech = mock<Mech>()
            val deploymentFeature = mock<DeploymentFeature> {
                on { deployMech(any(), any()) } doReturn expectedMech
            }
            val context = mock<CommandContext> {
                on { getFeature(DeploymentFeature::class.java) } doReturn Option.some(deploymentFeature)
            }
            val specification = newTestMechSpecification()
            val position = CellId(3, 6)
            val subject = DeployMechCommand(specification, position)

            // when
            val actualMech = subject.execute(context)

            // then
            Verify on deploymentFeature that deploymentFeature.deployMech(specification, position) was called
            actualMech shouldBe expectedMech
        }

        it("should throw exception when context does not provide required features") {
            // given
            val context = mock<CommandContext> {
                on { getFeature(DeploymentFeature::class.java) } doReturn Option.none()
            }
            val subject = DeployMechCommand(newTestMechSpecification(), CellId(3, 6))

            // when
            val func = { subject.execute(context) }

            // then
            val exceptionResult = func shouldThrow CommandException::class
            exceptionResult.exceptionMessage shouldContain DeploymentFeature::class.java.simpleName
        }
    }
})
