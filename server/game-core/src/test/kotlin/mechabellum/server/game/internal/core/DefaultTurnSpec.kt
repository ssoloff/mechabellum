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
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DefaultTurnSpec : Spek({
    describe("constructor") {
        it("should use default values that satisfy all preconditions") {
            // when: constructing an instance with default values
            DefaultTurn(id = TurnId(0))

            // then: it should not throw an exception
        }

        describe("initiative preconditions") {
            it("should throw exception when team absent from initiatives") {
                // when: not all teams have an initiative
                val operation = {
                    DefaultTurn(
                        id = TurnId(0),
                        initiativesByTeam = mapOf(Team.ATTACKER to Initiative.MIN)
                    )
                }

                // then: it should throw an exception
                operation
                    .shouldThrow(IllegalArgumentException::class)
                    .withMessage("expected initiative for team ${Team.DEFENDER} but was absent")
            }

            it("should throw exception when no team has won initiative") {
                // when: more than one team has the max initiative
                val operation = {
                    DefaultTurn(
                        id = TurnId(0),
                        initiativesByTeam = mapOf(Team.ATTACKER to Initiative.MIN, Team.DEFENDER to Initiative.MIN)
                    )
                }

                // then: it should throw an exception
                operation
                    .shouldThrow(IllegalArgumentException::class)
                    .withMessage("expected 1 team to have the max initiative (${Initiative.MIN}) but was 2 teams")
            }
        }
    }
})
