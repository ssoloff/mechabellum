// ktlint-disable filename
// (only necessary until a second command spec is added to this file)

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

import com.nhaarman.mockitokotlin2.mock
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.unit.Mech
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DeployCommandSpec : Spek({
    describe("execute") {
        it("should deploy Mech with specified position and facing") {
            // given: the deployment phase is active
            val deploymentPhase = mock<DeploymentPhase>()

            // when: the command is executed
            val mech = mock<Mech>()
            val position = Position(3, 6)
            val facing = Direction.NORTH
            val subject = DeployCommand(mech, position, facing)
            subject.execute(deploymentPhase)

            // then: it should deploy the Mech with the specified position and facing
            Verify on deploymentPhase that deploymentPhase.deploy(mech, position, facing) was called
        }
    }
})
