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

package mechabellum.server.game.api.core.commands.movement

import com.nhaarman.mockitokotlin2.mock
import mechabellum.server.game.api.core.grid.Angle
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Displacement
import mechabellum.server.game.api.core.phases.MovementPhase
import mechabellum.server.game.api.core.unit.Mech
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object MoveCommandSpec : Spek({
    describe("execute") {
        it("should move the specified displacement") {
            // given: the movement phase is active
            val movementPhase = mock<MovementPhase>()

            // when: the command is executed
            val displacement = Displacement(1, Direction.NORTHEAST)
            val subject = MoveCommand(displacement)
            subject.execute(movementPhase)

            // then: it should move the specified displacement
            Verify on movementPhase that movementPhase.move(displacement) was called
        }
    }
})

object SelectCommandSpec : Spek({
    describe("execute") {
        it("should select Mech") {
            // given: the movement phase is active
            val movementPhase = mock<MovementPhase>()

            // when: the command is executed
            val mech = mock<Mech>()
            val subject = SelectCommand(mech)
            subject.execute(movementPhase)

            // then: it should select the Mech
            Verify on movementPhase that movementPhase.select(mech) was called
        }
    }
})

object TurnCommandSpec : Spek({
    describe("execute") {
        it("should turn the specified angle") {
            // given: the movement phase is active
            val movementPhase = mock<MovementPhase>()

            // when: the command is executed
            val angle = Angle.ONE
            val subject = TurnCommand(angle)
            subject.execute(movementPhase)

            // then: it should turn the specified angle
            Verify on movementPhase that movementPhase.turn(angle) was called
        }
    }
})
