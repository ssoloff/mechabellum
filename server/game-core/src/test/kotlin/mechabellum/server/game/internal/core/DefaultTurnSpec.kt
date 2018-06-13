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
import mechabellum.server.game.api.core.TurnSpec
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DefaultTurnSpec : Spek({
    describe("initiativeWinner") {
        it("should return Some when initiative winner present") {
            // given: the attacker wins initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeam = mapOf(Team.ATTACKER to Initiative.MAX, Team.DEFENDER to Initiative.MIN)
            )

            // when: getting the initiative winner
            val initiativeWinner = subject.initiativeWinner

            // then: it should be the attacker
            initiativeWinner shouldEqual Option.some(Team.ATTACKER)
        }

        it("should return None when initiative results have not been set") {
            // given: initiative results have not been set
            val subject = DefaultTurn(id = TurnId(0), initiativesByTeam = mapOf())

            // when: getting the initiative winner
            val initiativeWinner = subject.initiativeWinner

            // then: it should be empty
            initiativeWinner shouldEqual Option.none()
        }
    }
})

object DefaultTurnBehavesAsTurnSpec : TurnSpec(
    newTurn = { initiativesByTeam -> DefaultTurn(id = TurnId(0), initiativesByTeam = initiativesByTeam) }
)
