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

package mechabellum.server.game.api.core.commands.deployment

import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.common.api.core.util.Result
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.unit.Mech
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DeployMechCommandSpec : Spek({
    describe("execute") {
        it("should deploy Mech to specified position and return Empty") {
            // given
            val deploymentPhase = mock<DeploymentPhase>()
            val mech = mock<Mech>()
            val position = Position(3, 6)
            val subject = DeployMechCommand(mech, position)

            // when
            val result = subject.execute(deploymentPhase)

            // then
            Verify on deploymentPhase that deploymentPhase.deployMech(mech, position) was called
            result shouldEqual Result.empty()
        }
    }
})
