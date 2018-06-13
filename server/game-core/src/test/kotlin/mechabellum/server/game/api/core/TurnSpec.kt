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

package mechabellum.server.game.api.core

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.common.api.test.DataClassSpec
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object TurnIdSpec : Spek({
    describe("constructor") {
        it("should throw exception when value is negative") {
            // when: value is negative
            val value = -1
            val operation = { TurnId(value) }

            // then: it should throw an exception
            operation shouldThrow IllegalArgumentException::class withMessage "expected value to be non-negative but was $value"
        }
    }
})

object TurnIdBehavesAsDataClassSpec : DataClassSpec({ TurnId(0) })

abstract class TurnSpec(val newTurn: (Map<Team, Initiative>) -> Turn) : Spek({
    describe("getInitiative") {
        val team = Team.ATTACKER

        it("should return Some when initiative present") {
            // given: a turn with initiative for team
            val initiative = Initiative.MAX
            val subject = newTurn(mapOf(team to initiative))

            // when: getting team initiative
            val initiativeOption = subject.getInitiative(team)

            // then: it should return team initiative
            initiativeOption shouldEqual Option.some(initiative)
        }

        it("should return None when initiative absent") {
            // given: a turn without initiative for team
            val subject = newTurn(mapOf())

            // when: getting team initiative
            val initiativeOption = subject.getInitiative(team)

            // then: it should return empty
            initiativeOption shouldEqual Option.none()
        }
    }
})
