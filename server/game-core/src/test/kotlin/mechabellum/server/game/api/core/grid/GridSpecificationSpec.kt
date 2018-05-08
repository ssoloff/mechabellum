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
        it("should throw exception when missing a team deployment zone") {
            // when
            val operation = {
                newTestGridSpecification().copy(
                    deploymentZonesByTeam = mapOf(Team.ATTACKER to CellRange(0..0, 0..0))
                )
            }

            // then
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "no deployment zone for team"
        }

        on(
            "deployment zone %s that exceeds grid bounds",
            data(CellRange(-1..0, 0..0), expected = Unit),
            data(CellRange(0..1, 0..0), expected = Unit),
            data(CellRange(0..0, -1..0), expected = Unit),
            data(CellRange(0..0, 0..1), expected = Unit)
        ) { invalidDeploymentZone, _ ->
            it("should throw exception") {
                // when
                val operation = {
                    newTestGridSpecification().copy(
                        deploymentZonesByTeam = mapOf(
                            Team.ATTACKER to invalidDeploymentZone,
                            Team.DEFENDER to CellRange(0..0, 0..0)
                        ),
                        type = newTestGridType().copy(cols = 1, rows = 1)
                    )
                }

                // then
                val exceptionResult = operation shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain "exceeds grid bounds"
            }
        }
    }
})

object GridSpecificationBehavesAsDataClassSpec : DataClassSpec({ newTestGridSpecification() })
