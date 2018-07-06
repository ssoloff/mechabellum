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
import mechabellum.server.game.api.core.TurnId
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
import kotlin.properties.Delegates

object DefaultGameBehavesAsGameSpec : GameSpec(::DefaultGame)

internal object DefaultGameStateSpec : SubjectSpek<DefaultGameState>({
    subject { DefaultGameState() }

    fun newMech(mechId: MechId): DefaultMech = DefaultMech(
        facing = Option.none(),
        id = mechId,
        position = Option.none(),
        team = Team.ATTACKER
    )

    describe("addMech") {
        it("should add Mech when ID absent") {
            // when: a Mech is added with an absent ID
            val expected = newMech(MechId(0))
            subject.addMech(expected)

            // then: the Mech should be added
            subject.getMech(expected.id) shouldBe expected
        }

        it("should throw exception when ID present") {
            // given: an existing Mech
            val mechId = MechId(0)
            subject.addMech(newMech(mechId))

            // when: a Mech is added with the same ID
            val operation = { subject.addMech(newMech(mechId)) }

            // then: an exception should be thrown
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }
    }

    describe("addTurn") {
        it("should add a turn with the next available ID") {
            // when: a turn is added
            val turnId0 = subject.addTurn()
            // and: another turn is added
            val turnId1 = subject.addTurn()

            // then: each turn should be available with the expected ID
            turnId0 shouldEqual TurnId(0)
            subject.getTurn(turnId0).id shouldEqual turnId0
            turnId1 shouldEqual TurnId(1)
            subject.getTurn(turnId1).id shouldEqual turnId1
        }
    }

    describe("getMech") {
        it("should return Mech when present") {
            // given: an existing Mech
            val mechId = MechId(0)
            val expected = newMech(mechId)
            subject.addMech(expected)

            // when: a present Mech is requested
            val actual = subject.getMech(mechId)

            // then: the requested Mech should be returned
            actual shouldBe expected
        }

        it("should throw exception when Mech absent") {
            // when: an absent Mech is requested
            val mechId = MechId(-1)
            val operation = { subject.getMech(mechId) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }
    }

    describe("getTurn") {
        it("should return turn when present") {
            // given: an existing turn
            val turnId = subject.addTurn()

            // when: a present turn is requested
            val turn = subject.getTurn(turnId)

            // then: the requested turn should be returned
            turn.id shouldEqual turnId
        }

        it("should throw exception when turn absent") {
            // when: an absent turn is requested
            val turnId = TurnId(999)
            val operation = { subject.getTurn(turnId) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain turnId.toString()
        }
    }

    describe("modifyMech") {
        it("should modify specified Mech and update game state") {
            // given: an existing Mech
            val mechId = MechId(0)
            subject.addMech(newMech(mechId))

            // when: the Mech is modified
            var mech: DefaultMech by Delegates.notNull()
            subject.modifyMech(mechId) {
                mech = it.setPosition(Position(5, 8))
                mech
            }

            // then: the modified Mech should be available on subsequent calls
            subject.getMech(mechId) shouldBe mech
        }

        it("should throw exception when Mech does not exist") {
            // when: an attempt is made to modify a non-existent Mech
            val mechId = MechId(-1)
            val operation = { subject.modifyMech(mechId) { it } }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }
    }

    describe("modifyTurn") {
        it("should modify specified turn and update game state") {
            // given: an existing turn
            val turnId = subject.addTurn()

            // when: the turn is modified
            var turn: DefaultTurn by Delegates.notNull()
            subject.modifyTurn(turnId) {
                turn = DefaultTurn(TurnId(999))
                turn
            }

            // then: the modified turn should be available on subsequent calls
            subject.getTurn(turnId) shouldBe turn
        }

        it("should throw exception when turn does not exist") {
            // when: an attempt is made to modify a non-existent turn
            val turnId = TurnId(999)
            val operation = { subject.modifyTurn(turnId) { it } }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain turnId.toString()
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
