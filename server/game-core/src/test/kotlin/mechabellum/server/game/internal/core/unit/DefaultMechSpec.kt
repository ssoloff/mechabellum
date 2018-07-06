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

package mechabellum.server.game.internal.core.unit

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.MechId
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DefaultMechSpec : Spek({
    describe("setFacing") {
        it("should set facing") {
            // given: a Mech
            val subject = DefaultMech(
                facing = Option.none(),
                id = MechId(0),
                position = Option.none(),
                team = Team.ATTACKER
            )

            // when: setting facing
            val newFacing = Direction.SOUTH
            val newSubject = subject.setFacing(newFacing)

            // then: it should return a Mech with the new facing
            newSubject.facing shouldEqual Option.some(newFacing)
        }
    }

    describe("setPosition") {
        it("should set position") {
            // given: a Mech
            val subject = DefaultMech(
                facing = Option.none(),
                id = MechId(0),
                position = Option.none(),
                team = Team.ATTACKER
            )

            // when: setting position
            val newPosition = Position(4, 8)
            val newSubject = subject.setPosition(newPosition)

            // then: it should return a Mech with the new position
            newSubject.position shouldEqual Option.some(newPosition)
        }
    }
})
