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

package mechabellum.server.game.api.core

import mechabellum.server.common.api.core.util.Option
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class UnitTypeRegistrySpec(
    absentMechTypeName: String,
    presentMechTypeName: String,
    subjectFactory: () -> UnitTypeRegistry
) : SubjectSpek<UnitTypeRegistry>({
    subject { subjectFactory() }

    describe("findMechTypeByName") {
        it("should return Some when Mech type present") {
            // when
            val mechTypeOption = subject.findMechTypeByName(presentMechTypeName)

            // then
            mechTypeOption shouldBeInstanceOf Option.Some::class
            with(mechTypeOption as Option.Some) {
                value.name shouldEqual presentMechTypeName
            }
        }

        it("should return None when Mech type absent") {
            subject.findMechTypeByName(absentMechTypeName) shouldEqual Option.none()
        }
    }
})
