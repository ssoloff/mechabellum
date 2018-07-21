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

package mechabellum.server.game.internal.core

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.internal.core.mechanics.InitiativeHistory
import mechabellum.server.game.internal.core.mechanics.MovementSelection
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeIn
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DefaultTurnSpec : Spek({
    describe("beginMovementSelection") {
        it("should add specified movement selection to movement selections and activate it") {
            // given: a turn
            val subject = DefaultTurn(
                id = TurnId(0),
                movementSelection = Option.none(),
                movementSelections = emptyList()
            )

            // when: beginning a movement selection
            val movementSelection = MovementSelection(MechId(0))
            val newSubject = subject.beginMovementSelection(movementSelection)

            // then: it should return a turn containing the specified movement selection
            movementSelection shouldBeIn newSubject.movementSelections
            // and: the specified movement selection should be active
            newSubject.movementSelection shouldEqual Option.some(movementSelection)
        }

        it("should throw exception when movement selection is active") {
            // given: a turn with an active movement selection
            val movementSelection1 = MovementSelection(MechId(0))
            val subject = DefaultTurn(
                id = TurnId(0),
                movementSelection = Option.some(movementSelection1),
                movementSelections = listOf(movementSelection1)
            )

            // when: beginning a movement selection
            val movementSelection2 = MovementSelection(MechId(1))
            val operation = { subject.beginMovementSelection(movementSelection2) }

            // then: it should throw an exception
            operation
                .shouldThrow(IllegalStateException::class)
                .withMessage("cannot begin movement selection for Mech with ID ${movementSelection2.mechId} when movement selection active for Mech with ID ${movementSelection1.mechId}")
        }
    }

    describe("endMovementSelection") {
        it("should end the active movement selection") {
            // given: a turn with an active movement selection
            val movementSelection = MovementSelection(MechId(0))
            val subject = DefaultTurn(
                id = TurnId(0),
                movementSelection = Option.some(movementSelection),
                movementSelections = listOf(movementSelection)
            )

            // when: ending the active movement selection
            val newSubject = subject.endMovementSelection()

            // then: it should return a turn with no active movement selection
            newSubject.movementSelection shouldEqual Option.none()
        }

        it("should throw exception when no active movement selection") {
            // given: a turn with no active movement selection
            val subject = DefaultTurn(
                id = TurnId(0),
                movementSelection = Option.none(),
                movementSelections = emptyList()
            )

            // when: ending the active movement selection
            val operation = { subject.endMovementSelection() }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "cannot end movement selection when no active movement selection"
        }
    }

    describe("setInitiativeHistory") {
        it("should set initiative history") {
            // given: a turn
            val subject = DefaultTurn(id = TurnId(0), initiativeHistory = InitiativeHistory())

            // when: setting initiative history
            val newInitiativeHistory = InitiativeHistory(listOf(mapOf(Team.ATTACKER to Initiative.MIN)))
            val newSubject = subject.setInitiativeHistory(newInitiativeHistory)

            // then: it should return a turn with the new initiative history
            newSubject.initiativeHistory shouldBe newInitiativeHistory
        }
    }
})
