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

import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.internal.core.mechanics.InitiativeHistory
import org.amshove.kluent.shouldBe
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DefaultTurnSpec : Spek({
    describe("setInitiativeHistory") {
        it("should set initiative history") {
            // given: a turn
            val subject = DefaultTurn(id = TurnId(0), initiativeHistory = InitiativeHistory())

            // when: setting initiative history
            val newInitiativeHistory = InitiativeHistory(listOf(mapOf(Team.ATTACKER to Initiative.MIN)))
            val newSubject = subject.setInitiativeHistory(newInitiativeHistory)

            // then: it should return a turn using the new initiative history
            newSubject.initiativeHistory shouldBe newInitiativeHistory
        }
    }
})
