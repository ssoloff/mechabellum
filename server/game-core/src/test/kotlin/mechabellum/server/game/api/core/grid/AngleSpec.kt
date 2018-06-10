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

import mechabellum.server.common.api.test.ComparableSpec
import mechabellum.server.common.api.test.DataClassSpec
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

object AngleSpec : Spek({
    describe("isNormalized") {
        on(
            "%s",
            data(Angle(Int.MIN_VALUE), expected = false),
            data(Angle(-1), expected = false),
            data(Angle(0), expected = true),
            data(Angle(1), expected = true),
            data(Angle(2), expected = true),
            data(Angle(3), expected = true),
            data(Angle(4), expected = true),
            data(Angle(5), expected = true),
            data(Angle(6), expected = false),
            data(Angle(Int.MAX_VALUE), expected = false)
        ) { angle, expected ->
            it("should return $expected") {
                angle.isNormalized() shouldEqual expected
            }
        }
    }

    describe("minus") {
        on(
            "%s - %s",
            data(Angle(-1), Angle(-1), expected = Angle(0)),
            data(Angle(-1), Angle(0), expected = Angle(-1)),
            data(Angle(-1), Angle(1), expected = Angle(-2)),
            data(Angle(0), Angle(-1), expected = Angle(1)),
            data(Angle(0), Angle(0), expected = Angle(0)),
            data(Angle(0), Angle(1), expected = Angle(-1)),
            data(Angle(1), Angle(-1), expected = Angle(2)),
            data(Angle(1), Angle(0), expected = Angle(1)),
            data(Angle(1), Angle(1), expected = Angle(0))
        ) { first, second, expected ->
            it("should return $expected") {
                first - second shouldEqual expected
            }
        }
    }

    describe("normalize") {
        on(
            "%s",
            data(Angle(Int.MIN_VALUE), expected = Angle(4)),
            data(Angle(-6), expected = Angle(0)),
            data(Angle(-5), expected = Angle(1)),
            data(Angle(-4), expected = Angle(2)),
            data(Angle(-3), expected = Angle(3)),
            data(Angle(-2), expected = Angle(4)),
            data(Angle(-1), expected = Angle(5)),
            data(Angle(0), expected = Angle(0)),
            data(Angle(1), expected = Angle(1)),
            data(Angle(2), expected = Angle(2)),
            data(Angle(3), expected = Angle(3)),
            data(Angle(4), expected = Angle(4)),
            data(Angle(5), expected = Angle(5)),
            data(Angle(6), expected = Angle(0)),
            data(Angle(7), expected = Angle(1)),
            data(Angle(8), expected = Angle(2)),
            data(Angle(9), expected = Angle(3)),
            data(Angle(10), expected = Angle(4)),
            data(Angle(11), expected = Angle(5)),
            data(Angle(Int.MAX_VALUE), expected = Angle(1))
        ) { angle, expected ->
            it("should return $expected") {
                angle.normalize() shouldEqual expected
            }
        }
    }

    describe("plus") {
        on(
            "%s + %s",
            data(Angle(-1), Angle(-1), expected = Angle(-2)),
            data(Angle(-1), Angle(0), expected = Angle(-1)),
            data(Angle(-1), Angle(1), expected = Angle(0)),
            data(Angle(0), Angle(-1), expected = Angle(-1)),
            data(Angle(0), Angle(0), expected = Angle(0)),
            data(Angle(0), Angle(1), expected = Angle(1)),
            data(Angle(1), Angle(-1), expected = Angle(0)),
            data(Angle(1), Angle(0), expected = Angle(1)),
            data(Angle(1), Angle(1), expected = Angle(2))
        ) { first, second, expected ->
            it("should return $expected") {
                first + second shouldEqual expected
            }
        }
    }

    describe("unaryMinus") {
        on(
            "-%s",
            data(Angle(-1), expected = Angle(1)),
            data(Angle(0), expected = Angle(0)),
            data(Angle(1), expected = Angle(-1))
        ) { angle, expected ->
            it("should return $expected") {
                -angle shouldEqual expected
            }
        }
    }
})

object AngleBehavesAsComparableSpec : ComparableSpec<Angle>(
    newGreaterThanInstances = { listOf(Angle(2), Angle(42)) },
    newLessThanInstances = { listOf(Angle(-42), Angle(0)) },
    newReferenceInstance = { Angle(1) }
)

object AngleBehavesAsDataClassSpec : DataClassSpec({ Angle(1) })
