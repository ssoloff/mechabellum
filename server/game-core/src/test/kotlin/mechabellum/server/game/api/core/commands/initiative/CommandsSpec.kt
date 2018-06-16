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

import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.InitiativePhase
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object RollInitiativeCommandSpec : Spek({
    describe("execute") {
        it("should roll initiative") {
            // given: the initiative phase is active
            val initiativePhase = mock<InitiativePhase>()

            // when: the command is executed
            val team = Team.ATTACKER
            val subject = RollInitiativeCommand(team)
            subject.execute(initiativePhase)

            // then: it should roll initiative
            Verify on initiativePhase that initiativePhase.rollInitiative(team) was called
        }
    }
})
