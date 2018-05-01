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

package mechabellum.server.game.api.core.phases

import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldNotEqual
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

abstract class InitializationPhaseSpec(
    getMech: (InitializationPhase, MechId) -> Mech,
    subjectFactory: () -> InitializationPhase
) : SubjectSpek<InitializationPhase>({
    subject { subjectFactory() }

    describe("newMech") {
        it("should add Mech to game") {
            // when
            val mech = subject.newMech(newTestMechSpecification())

            // then
            getMech(subject, mech.id) shouldBe mech
        }

        it("should create Mechs with distinct identifiers") {
            // when
            val mech1 = subject.newMech(newTestMechSpecification())
            val mech2 = subject.newMech(newTestMechSpecification())

            // then
            mech1.id shouldNotEqual mech2.id
        }
    }
})
