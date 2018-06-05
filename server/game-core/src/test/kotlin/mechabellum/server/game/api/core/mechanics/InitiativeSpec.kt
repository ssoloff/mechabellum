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

package mechabellum.server.game.api.core.mechanics

import mechabellum.server.common.api.test.ComparableSpec
import mechabellum.server.common.api.test.DataClassSpec
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

object InitiativeSpec : Spek({
    describe("constructor") {
        on(
            "out-of-range value %s",
            data(Initiative.MIN.value - 1, expected = Unit),
            data(Initiative.MAX.value + 1, expected = Unit)
        ) { value, _ ->
            it("should throw exception") {
                // when: initiative is constructed with out-of-range value
                val operation = { Initiative(value) }

                // then: it should throw an exception
                operation
                    .shouldThrow(IllegalArgumentException::class)
                    .withMessage("expected value in range [${Initiative.MIN.value},${Initiative.MAX.value}] but was $value")
            }
        }
    }
})

object InitiativeBehavesAsComparableSpec : ComparableSpec<Initiative>(
    newGreaterThanInstances = { listOf(Initiative(7), Initiative(8)) },
    newLessThanInstances = { listOf(Initiative(4), Initiative(5)) },
    newReferenceInstance = { Initiative(6) }
)

object InitiativeBehavesAsDataClassSpec : DataClassSpec({ Initiative.MIN })
