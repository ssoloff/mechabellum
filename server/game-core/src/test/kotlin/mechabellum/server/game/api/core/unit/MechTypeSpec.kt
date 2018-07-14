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

package mechabellum.server.game.api.core.unit

import mechabellum.server.common.api.test.DataClassSpec
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object MechTypeSpec : Spek({
    describe("constructor") {
        it("should throw exception when movement points is non-positive") {
            // when: creating a Mech type with non-positive movement points
            val movementPoints = 0
            val operation = { newTestMechType().copy(movementPoints = movementPoints) }

            // then: it should throw an exception
            operation shouldThrow IllegalArgumentException::class withMessage "expected movement points to be positive but was $movementPoints"
        }
    }
})

object MechTypeBehavesAsDataClassSpec : DataClassSpec({ newTestMechType() })
