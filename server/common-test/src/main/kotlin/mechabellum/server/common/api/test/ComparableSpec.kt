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

package mechabellum.server.common.api.test

import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeLessThan
import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/** Verifies an implementation of [Comparable] conforms to the interface specification. */
abstract class ComparableSpec<T : Comparable<T>>(
    newGreaterThanInstances: () -> Collection<T>,
    newLessThanInstances: () -> Collection<T>,
    newReferenceInstance: () -> T
) : Spek({
    describe("spec implementation") {
        it("should provide at least one greater than instance") {
            newGreaterThanInstances().size shouldBeGreaterThan 0
        }

        it("should provide at least one less than instance") {
            newLessThanInstances().size shouldBeGreaterThan 0
        }
    }

    describe("compareTo") {
        it("should return zero when target instance is equal to other instance") {
            val target = newReferenceInstance()
            val other = newReferenceInstance()
            target.compareTo(other) shouldEqualTo 0
        }

        it("should return less than zero when target instance is less than other instance") {
            val target = newReferenceInstance()
            val others = newGreaterThanInstances()
            others.forEach { other -> target.compareTo(other) shouldBeLessThan 0 }
        }

        it("should return greater than zero when target instance is greater than other instance") {
            val target = newReferenceInstance()
            val others = newLessThanInstances()
            others.forEach { other -> target.compareTo(other) shouldBeGreaterThan 0 }
        }
    }
})
