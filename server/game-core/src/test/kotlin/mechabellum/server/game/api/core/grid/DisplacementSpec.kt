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
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DisplacementSpec : Spek({
    describe("constructor") {
        it("should throw exception when magnitude is negative") {
            // when: constructing a displacement with a negative magnitude
            val magnitude = -1
            val operation = { Displacement(magnitude, Direction.NORTH) }

            // then: it should throw an exception
            operation shouldThrow IllegalArgumentException::class withMessage "expected magnitude to be non-negative but was $magnitude"
        }
    }
})

object DisplacementBehavesAsDataClassSpec : DataClassSpec({ Displacement(1, Direction.NORTH) })
