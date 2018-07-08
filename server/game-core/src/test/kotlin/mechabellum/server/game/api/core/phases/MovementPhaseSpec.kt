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
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.newTestGameSpecification
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.newTestMechSpecification
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
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
                    deploymentPositionsByTeam = mapOf(
                        Team.ATTACKER to Position(0, 0)..Position(5, 5),
                        Team.DEFENDER to Position(0, 0)..Position(5, 5)
                    ),
                    type = newTestGridType().copy(cols = 6, rows = 6)
                )
            )
        )
    }

    describe("move") {
        on(
            "initial position=(3,3), magnitude=%s, direction=%s",
            data(-2, Direction.NORTH, expected = Position(3, 5)),
            data(-2, Direction.NORTHEAST, expected = Position(1, 4)),
            data(-2, Direction.SOUTHEAST, expected = Position(1, 2)),
            data(-2, Direction.SOUTH, expected = Position(3, 1)),
            data(-2, Direction.SOUTHWEST, expected = Position(5, 2)),
            data(-2, Direction.NORTHWEST, expected = Position(5, 4)),
            data(-1, Direction.NORTH, expected = Position(3, 4)),
            data(-1, Direction.NORTHEAST, expected = Position(2, 4)),
            data(-1, Direction.SOUTHEAST, expected = Position(2, 3)),
            data(-1, Direction.SOUTH, expected = Position(3, 2)),
            data(-1, Direction.SOUTHWEST, expected = Position(4, 3)),
            data(-1, Direction.NORTHWEST, expected = Position(4, 4)),
            data(0, Direction.NORTH, expected = Position(3, 3)),
            data(1, Direction.NORTH, expected = Position(3, 2)),
            data(1, Direction.NORTHEAST, expected = Position(4, 3)),
            data(1, Direction.SOUTHEAST, expected = Position(4, 4)),
            data(1, Direction.SOUTH, expected = Position(3, 4)),
            data(1, Direction.SOUTHWEST, expected = Position(2, 4)),
            data(1, Direction.NORTHWEST, expected = Position(2, 3)),
            data(2, Direction.NORTH, expected = Position(3, 1)),
            data(2, Direction.NORTHEAST, expected = Position(5, 2)),
            data(2, Direction.SOUTHEAST, expected = Position(5, 4)),
            data(2, Direction.SOUTH, expected = Position(3, 5)),
            data(2, Direction.SOUTHWEST, expected = Position(1, 4)),
            data(2, Direction.NORTHWEST, expected = Position(1, 2))
        ) { magnitude, direction, expected ->
            it("should move Mech to position $expected") {
                // given: a Mech associated with the moving team positioned in cell (3,3) and facing in direction of movement
                val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
                strategy.deploy(mech, Position(3, 3), direction)

                // when: moving the Mech by the specified displacement
                subject.move(mech, magnitude, direction)

                // then: it should be positioned in the expected cell
                strategy.getMech(mech.id).position shouldEqual Option.some(expected)
            }
        }

        on(
            "initial position=(2,2), magnitude=%s, direction=%s",
            data(-2, Direction.NORTH, expected = Position(2, 4)),
            data(-2, Direction.NORTHEAST, expected = Position(0, 3)),
            data(-2, Direction.SOUTHEAST, expected = Position(0, 1)),
            data(-2, Direction.SOUTH, expected = Position(2, 0)),
            data(-2, Direction.SOUTHWEST, expected = Position(4, 1)),
            data(-2, Direction.NORTHWEST, expected = Position(4, 3)),
            data(-1, Direction.NORTH, expected = Position(2, 3)),
            data(-1, Direction.NORTHEAST, expected = Position(1, 2)),
            data(-1, Direction.SOUTHEAST, expected = Position(1, 1)),
            data(-1, Direction.SOUTH, expected = Position(2, 1)),
            data(-1, Direction.SOUTHWEST, expected = Position(3, 1)),
            data(-1, Direction.NORTHWEST, expected = Position(3, 2)),
            data(0, Direction.NORTH, expected = Position(2, 2)),
            data(1, Direction.NORTH, expected = Position(2, 1)),
            data(1, Direction.NORTHEAST, expected = Position(3, 1)),
            data(1, Direction.SOUTHEAST, expected = Position(3, 2)),
            data(1, Direction.SOUTH, expected = Position(2, 3)),
            data(1, Direction.SOUTHWEST, expected = Position(1, 2)),
            data(1, Direction.NORTHWEST, expected = Position(1, 1)),
            data(2, Direction.NORTH, expected = Position(2, 0)),
            data(2, Direction.NORTHEAST, expected = Position(4, 1)),
            data(2, Direction.SOUTHEAST, expected = Position(4, 3)),
            data(2, Direction.SOUTH, expected = Position(2, 4)),
            data(2, Direction.SOUTHWEST, expected = Position(0, 3)),
            data(2, Direction.NORTHWEST, expected = Position(0, 1))
        ) { magnitude, direction, expected ->
            it("should move Mech to position $expected") {
                // given: a Mech associated with the moving team positioned in cell (2,2) and facing in direction of movement
                val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
                strategy.deploy(mech, Position(2, 2), direction)

                // when: moving the Mech by the specified displacement
                subject.move(mech, magnitude, direction)

                // then: it should be positioned in the expected cell
                strategy.getMech(mech.id).position shouldEqual Option.some(expected)
            }
        }

        it("should throw exception when Mech does not exist") {
            // given: a Mech that was not created by the game
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
                on { this.team } doReturn team
            }

            // when: moving the Mech
            val operation = { subject.move(mech, 1, Direction.NORTH) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }

        it("should throw exception when Mech belongs to a different team") {
            // given: a Mech not associated with the moving team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when: moving the Mech
            val operation = { subject.move(mech, 1, Direction.NORTH) }

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

        it("should throw exception when Mech does not exist") {
            // given: a Mech that was not created by the game
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
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
    }
}) {
    interface Strategy {
        val game: Game

        fun deploy(mech: Mech, position: Position, facing: Direction)

        fun getMech(mechId: MechId): Mech

        fun newMech(mechSpecification: MechSpecification): Mech
    }
}
