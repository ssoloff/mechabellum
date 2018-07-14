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

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.grid.Angle
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Displacement
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.newTestGameSpecification
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import mechabellum.server.game.api.core.unit.newTestMechType
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import kotlin.properties.Delegates

abstract class MovementPhaseSpec(
    newStrategy: (GameSpecification) -> Strategy,
    newSubject: (Strategy, Team) -> MovementPhase
) : SubjectSpek<MovementPhase>({
    var strategy: Strategy by Delegates.notNull()
    val team = Team.DEFENDER

    subject { newSubject(strategy, team) }

    beforeEachTest {
        strategy = newStrategy(
            newTestGameSpecification().copy(
                gridSpecification = newTestGridSpecification().copy(
                    type = newTestGridType().copy(cols = 3, rows = 3)
                )
            )
        )
    }

    describe("move") {
        it("should move Mech by specified displacement") {
            // given: a Mech associated with the moving team positioned in cell (0,0) and facing southeast
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
            strategy.deploy(mech, Position(0, 0), Direction.SOUTHEAST)

            // when: moving the Mech 2 cells to the southeast
            subject.move(mech, Displacement(2, Direction.SOUTHEAST))

            // then: it should be positioned in cell (2,1)
            strategy.getMech(mech.id).position shouldEqual Option.some(Position(2, 1))
        }

        it("should throw exception when Mech does not exist") {
            // given: a Mech that was not created by the game
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
                on { this.team } doReturn team
            }

            // when: moving the Mech
            val operation = { subject.move(mech, Displacement(1, Direction.NORTH)) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }

        it("should throw exception when Mech belongs to a different team") {
            // given: a Mech not associated with the moving team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when: moving the Mech
            val operation = { subject.move(mech, Displacement(1, Direction.NORTH)) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "team"
        }
    }

    describe("turn") {
        it("should turn Mech by specified angle") {
            // given: a Mech associated with the moving team and facing north
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
            strategy.deploy(mech, Position(0, 0), Direction.NORTH)

            // when: turning the Mech +2 sextants
            subject.turn(mech, Angle.TWO)

            // then: it should be facing southeast
            strategy.getMech(mech.id).facing shouldEqual Option.some(Direction.SOUTHEAST)
        }

        it("should reduce Mech's available movement points by one per sextant turned") {
            // given: a Mech associated with the moving team and having six movement points
            val mech = strategy.newMech(
                newTestMechSpecification().copy(
                    team = team,
                    type = newTestMechType().copy(movementPoints = 6)
                )
            )
            strategy.deploy(mech, Position(0, 0), Direction.NORTH)

            // when: turning the Mech +2 sextants
            subject.turn(mech, Angle.TWO)

            // then: it should have four movement points
            strategy.getMech(mech.id).movementPoints shouldEqual 4
        }

        it("should throw exception when Mech does not exist") {
            // given: a Mech that was not created by the game
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
                on { movementPoints } doReturn 1
                on { this.team } doReturn team
            }

            // when: turning the Mech
            val operation = { subject.turn(mech, Angle.ONE) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }

        it("should throw exception when Mech belongs to a different team") {
            // given: a Mech not associated with the moving team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when: turning the Mech
            val operation = { subject.turn(mech, Angle.ONE) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "team"
        }

        it("should throw exception when Mech has insufficient movement points") {
            // given: a Mech associated with the moving team and having one movement point
            val mech = strategy.newMech(
                newTestMechSpecification().copy(
                    team = team,
                    type = newTestMechType().copy(movementPoints = 1)
                )
            )
            strategy.deploy(mech, Position(0, 0), Direction.NORTH)

            // when: turning the Mech +2 sextants
            val operation = { subject.turn(mech, Angle.TWO) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "movement points"
        }
    }
}) {
    interface Strategy {
        val game: Game

        fun deploy(mech: Mech, position: Position, facing: Direction)

        fun getMech(mechId: MechId): Mech

        fun newMech(mechSpecification: MechSpecification): Mech
    }
}
