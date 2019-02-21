/*
 * Copyright (C) 2019 Mechabellum contributors
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

import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.amshove.kluent.shouldBeLessOrEqualTo
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class DieRollerSpec(newSubject: (Int) -> DieRoller) : SubjectSpek<DieRoller>({
    val valueCount = 100

    subject { newSubject(valueCount) }

    describe("roll") {
        it("should return an integer between 1 and 6 inclusive") {
            for (i in 1..valueCount) {
                val roll = subject.roll()
                roll shouldBeGreaterOrEqualTo 1
                roll shouldBeLessOrEqualTo 6
            }
        }
    }
})

object UniformDieRollerBehavesAsDieRollerSpec : DieRollerSpec({ UniformDieRoller() })
