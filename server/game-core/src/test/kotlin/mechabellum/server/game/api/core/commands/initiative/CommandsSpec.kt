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

package mechabellum.server.game.api.core.commands.initiative

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.InitiativePhase
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object RollInitiativeCommandSpec : Spek({
    describe("execute") {
        it("should roll initiative and return the result") {
            // given: the initiative phase is active
            val team = Team.ATTACKER
            val expected = Initiative.MIN
            val initiativePhase = mock<InitiativePhase> {
                on { rollInitiative(team) } doReturn expected
            }

            // when: the command is executed
            val subject = RollInitiativeCommand(team)
            val actual = subject.execute(initiativePhase)

            // then: it should roll initiative
            Verify on initiativePhase that initiativePhase.rollInitiative(team) was called
            // and: return the result
            actual shouldEqual expected
        }
    }
})
