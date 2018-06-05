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

package mechabellum.server.game.api.core.grid

import mechabellum.server.common.api.test.DataClassSpec
import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

object GridSpecificationSpec : Spek({
    describe("constructor") {
        it("should throw exception when missing team deployment positions") {
            // when
            val operation = {
                newTestGridSpecification().copy(
                    deploymentPositionsByTeam = mapOf(Team.ATTACKER to Position(0, 0)..Position(0, 0))
                )
            }

            // then
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "expected deployment positions for team ${Team.DEFENDER}"
        }

        on(
            "deployment positions %s that exceeds grid bounds",
            data(Position(-1, 0)..Position(0, 0), expected = Unit),
            data(Position(0, 0)..Position(1, 0), expected = Unit),
            data(Position(0, -1)..Position(0, 0), expected = Unit),
            data(Position(0, 0)..Position(0, 1), expected = Unit)
        ) { invalidDeploymentPositions, _ ->
            it("should throw exception") {
                // when
                val operation = {
                    newTestGridSpecification().copy(
                        deploymentPositionsByTeam = mapOf(
                            Team.ATTACKER to invalidDeploymentPositions,
                            Team.DEFENDER to Position(0, 0)..Position(0, 0)
                        ),
                        type = newTestGridType().copy(cols = 1, rows = 1)
                    )
                }

                // then
                val exceptionResult = operation shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "but exceeded grid bounds"
            }
        }
    }
})

object GridSpecificationBehavesAsDataClassSpec : DataClassSpec({ newTestGridSpecification() })
