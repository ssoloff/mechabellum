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

package mechabellum.server.game.internal.core

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

internal object InternalGameBehavesAsDeploymentFeatureSpec : SubjectSpek<InternalGame>({
    subject { InternalGame() }

    describe("deployMech") {
        it("should add Mech to game when Mech ID does not exist") {
            // given
            val mechId = MechId(42)
            val mech = mock<Mech> {
                on { id } doReturn mechId
            }

            // when
            subject.deployMech(mech, CellId(3, 6))

            // then
            subject.getMech(mechId) shouldBe mech
        }

        it("should throw exception when Mech ID exists") {
            // given
            val mechId = MechId(42)
            val mech1 = mock<Mech> {
                on { id } doReturn mechId
            }
            val mech2 = mock<Mech> {
                on { id } doReturn mechId
            }

            // when
            subject.deployMech(mech1, CellId(3, 6))
            val func = { subject.deployMech(mech2, CellId(2, 4)) }

            // then
            func shouldThrow IllegalArgumentException::class
        }

        it("should place Mech at specified position") {
            // given
            val mechId = MechId(42)
            val mech = mock<Mech> {
                on { id } doReturn mechId
            }
            val position = CellId(3, 6)

            // when
            subject.deployMech(mech, position)

            // then
            subject.getMechPosition(mechId) shouldBe position
        }
    }
})
