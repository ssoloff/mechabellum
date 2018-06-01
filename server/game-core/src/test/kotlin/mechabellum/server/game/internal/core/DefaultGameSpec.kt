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

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.GameSpec
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.internal.core.unit.DefaultMech
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

object DefaultGameBehavesAsGameSpec : GameSpec(::DefaultGame)

internal object DefaultGameStateSpec : SubjectSpek<DefaultGameState>({
    subject { DefaultGameState() }

    fun newMechRecord(mechId: MechId): DefaultGameState.MechRecord = DefaultGameState.MechRecord(
        mech = DefaultMech(mechId, Team.ATTACKER),
        position = Option.none()
    )

    describe("addMechRecord") {
        it("should add Mech record when ID absent") {
            // when: a Mech record is added with an absent ID
            val expected = newMechRecord(MechId(0))
            subject.addMechRecord(expected)

            // then: the Mech record should be added
            subject.getMechRecord(expected.mech.id) shouldBe expected
        }

        it("should throw exception when ID present") {
            // given: an existing Mech record
            val mechId = MechId(0)
            subject.addMechRecord(newMechRecord(mechId))

            // when: a Mech record is added with the same ID
            val operation = { subject.addMechRecord(newMechRecord(mechId)) }

            // then: an exception should be thrown
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }
    }

    describe("getMechRecord") {
        it("should return Mech record when present") {
            // given: an existing Mech record
            val mechId = MechId(0)
            val expected = newMechRecord(mechId)
            subject.addMechRecord(expected)

            // when: a present Mech record is requested
            val actual = subject.getMechRecord(mechId)

            // then: the requested Mech record should be returned
            actual shouldBe expected
        }

        it("should throw exception when record absent") {
            // when: an absent Mech record is requested
            val mechId = MechId(-1)
            val operation = { subject.getMechRecord(mechId) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }
    }

    describe("modifyMechRecord") {
        it("should modify specified record and update game state") {
            // given: an existing Mech record
            val mechId = MechId(0)
            subject.addMechRecord(newMechRecord(mechId))

            // when: the Mech record is modified
            val newPosition = Position(5, 8)
            subject.modifyMechRecord(mechId) { it.setPosition(newPosition) }

            // then: the modified Mech record should be available on subsequent calls
            subject.getMechRecord(mechId).position shouldEqual Option.some(newPosition)
        }

        it("should throw exception when record does not exist") {
            // when: an attempt is made to modify a non-existent Mech record
            val mechId = MechId(-1)
            val operation = { subject.modifyMechRecord(mechId) { it } }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }
    }

    describe("newMechId") {
        it("should return distinct identifiers on subsequent calls") {
            // when: multiple Mech IDs are generated
            val mechId1 = subject.newMechId()
            val mechId2 = subject.newMechId()

            // then: they should be distinct
            mechId1 shouldNotEqual mechId2
        }
    }
})
