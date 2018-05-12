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

package mechabellum.server.common.api.test.ranges

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/** Verifies an implementation of [ClosedRange] conforms to the interface specification. */
abstract class ClosedRangeSpec<T : Comparable<T>>(
    inRangeValue: T,
    newEmptyInstance: () -> ClosedRange<T>,
    newNonEmptyInstance: () -> ClosedRange<T>,
    outOfRangeValue: T
) : Spek({
    describe("contains") {
        it("should return true when value is in range") {
            newNonEmptyInstance().contains(inRangeValue) shouldEqual true
        }

        it("should return false when value is out of range") {
            newNonEmptyInstance().contains(outOfRangeValue) shouldEqual false
        }
    }

    describe("isEmpty") {
        it("should return true when range is empty") {
            newEmptyInstance().isEmpty() shouldEqual true
        }

        it("should return false when range is not empty") {
            newNonEmptyInstance().isEmpty() shouldEqual false
        }
    }
})
