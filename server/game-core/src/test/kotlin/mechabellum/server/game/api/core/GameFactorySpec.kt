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

import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.MechType
import org.amshove.kluent.shouldNotEqual
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class GameFactorySpec(subjectFactory: () -> GameFactory) : SubjectSpek<GameFactory>({
    subject { subjectFactory() }

    describe("newMech") {
        it("should return instances with different identifiers") {
            // when
            val mechSpecification = MechSpecification(MechType("MechType"))
            val mech1 = subject.newMech(mechSpecification)
            val mech2 = subject.newMech(mechSpecification)

            // then
            mech1.id shouldNotEqual mech2.id
        }
    }
})
