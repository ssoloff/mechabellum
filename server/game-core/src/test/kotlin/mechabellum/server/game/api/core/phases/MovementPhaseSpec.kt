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
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
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

    describe("end") {
        it("should change active phase to defender movement phase when attacker is moving and at least one defender Mech can move") {
            // given: the attacker and defender each have one Mech
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // and: the attacker movement phase is active
            val subject = newSubject(strategy, Team.ATTACKER)
            // and: a movement selection has been made for the attacker Mech
            subject.select(attacker)
            // but: a movement selection has not been made for the defender Mech

            // when: movement phase is ended
            subject.end()

            // then: defender movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.DEFENDER
        }

        it("should change active phase to attacker movement phase when defender is moving and at least one attacker Mech can move") {
            // given: the attacker and defender each have one Mech
            strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            val defender = strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // and: the defender movement phase is active
            val subject = newSubject(strategy, Team.DEFENDER)
            // and: a movement selection has been made for the defender Mech
            subject.select(defender)
            // but: a movement selection has not been made for the attacker Mech

            // when: movement phase is ended
            subject.end()

            // then: attacker movement phase should be active
            strategy.game.phase shouldBeInstanceOf MovementPhase::class
            (strategy.game.phase as MovementPhase).team shouldEqual Team.ATTACKER
        }

        it("should change active phase to defender weapon attack phase when attacker is moving and no defender Mech can move and attacker won initiative") {
            // given: the attacker and defender each have one Mech
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            val defender = strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // and: the attacker movement phase is active
            val subject = newSubject(strategy, Team.ATTACKER)
            // and: a movement selection has been made for the defender Mech
            strategy.addMovementSelection(defender)
            // and: a movement selection has been made for the attacker Mech
            subject.select(attacker)
            // and: the attacker won initiative
            strategy.setInitiativeWinner(Team.ATTACKER)

            // when: movement phase is ended
            subject.end()

            // then: defender weapon attack phase should be active
            strategy.game.phase shouldBeInstanceOf WeaponAttackPhase::class
            (strategy.game.phase as WeaponAttackPhase).team shouldEqual Team.DEFENDER
        }

        it("should change active phase to attacker weapon attack phase when attacker is moving and no defender Mech can move and defender won initiative") {
            // given: the attacker and defender each have one Mech
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            val defender = strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // and: the attacker movement phase is active
            val subject = newSubject(strategy, Team.ATTACKER)
            // and: a movement selection has been made for the defender Mech
            strategy.addMovementSelection(defender)
            // and: a movement selection has been made for the attacker Mech
            subject.select(attacker)
            // and: the defender won initiative
            strategy.setInitiativeWinner(Team.DEFENDER)

            // when: movement phase is ended
            subject.end()

            // then: attacker weapon attack phase should be active
            strategy.game.phase shouldBeInstanceOf WeaponAttackPhase::class
            (strategy.game.phase as WeaponAttackPhase).team shouldEqual Team.ATTACKER
        }

        it("should change active phase to attacker weapon attack phase when defender is moving and no attacker Mech can move and defender won initiative") {
            // given: the attacker and defender each have one Mech
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            val defender = strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // and: the defender movement phase is active
            val subject = newSubject(strategy, Team.DEFENDER)
            // and: a movement selection has been made for the attacker Mech
            strategy.addMovementSelection(attacker)
            // and: a movement selection has been made for the defender Mech
            subject.select(defender)
            // and: the defender won initiative
            strategy.setInitiativeWinner(Team.DEFENDER)

            // when: movement phase is ended
            subject.end()

            // then: attacker weapon attack phase should be active
            strategy.game.phase shouldBeInstanceOf WeaponAttackPhase::class
            (strategy.game.phase as WeaponAttackPhase).team shouldEqual Team.ATTACKER
        }

        it("should change active phase to defender weapon attack phase when defender is moving and no attacker Mech can move and attacker won initiative") {
            // given: the attacker and defender each have one Mech
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            val defender = strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // and: the defender movement phase is active
            val subject = newSubject(strategy, Team.DEFENDER)
            // and: a movement selection has been made for the attacker Mech
            strategy.addMovementSelection(attacker)
            // and: a movement selection has been made for the defender Mech
            subject.select(defender)
            // and: the attacker won initiative
            strategy.setInitiativeWinner(Team.ATTACKER)

            // when: movement phase is ended
            subject.end()

            // then: defender weapon attack phase should be active
            strategy.game.phase shouldBeInstanceOf WeaponAttackPhase::class
            (strategy.game.phase as WeaponAttackPhase).team shouldEqual Team.DEFENDER
        }

        it("should clear selected Mech") {
            // given: the attacker and defender each have one Mech
            val attacker = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))
            strategy.newMech(newTestMechSpecification().copy(team = Team.DEFENDER))
            // and: the attacker movement phase is active
            val subject = newSubject(strategy, Team.ATTACKER)
            // and: the attacker Mech has been selected for movement
            subject.select(attacker)

            // when: movement phase is ended
            subject.end()

            // then: it should clear the selected Mech
            strategy.selectedMech shouldEqual Option.none()
        }

        it("should throw exception when no Mech selected for movement") {
            // given: no Mech is selected

            // when: movement phase is ended
            val operation = { subject.end() }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "no active movement selection"
        }
    }

    describe("move") {
        it("should move Mech by specified displacement") {
            // given: a Mech associated with the moving team positioned in cell (0,0) and facing southeast
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
            strategy.deploy(mech, Position(0, 0), Direction.SOUTHEAST)
            // and: the Mech has been selected for movement
            subject.select(mech)

            // when: moving the Mech 2 cells to the southeast
            subject.move(Displacement(2, Direction.SOUTHEAST))

            // then: it should be positioned in cell (2,1)
            strategy.getMech(mech.id).position shouldEqual Option.some(Position(2, 1))
        }

        it("should reduce Mech's available movement points by one per cell moved") {
            // given: a Mech associated with the moving team and having six movement points
            val mech = strategy.newMech(
                newTestMechSpecification().copy(
                    team = team,
                    type = newTestMechType().copy(movementPoints = 6)
                )
            )
            strategy.deploy(mech, Position(0, 0), Direction.SOUTH)
            // and: the Mech has been selected for movement
            subject.select(mech)

            // when: moving the Mech forward 2 cells
            subject.move(Displacement(2, Direction.SOUTH))

            // then: it should have four movement points
            strategy.getMech(mech.id).movementPoints shouldEqual 4
        }

        it("should throw exception when no Mech selected for movement") {
            // given: a Mech associated with the moving team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
            strategy.deploy(mech, Position(0, 0), Direction.SOUTHEAST)
            // but: no Mech has been selected for movement

            // when: moving the Mech
            val operation = { subject.move(Displacement(2, Direction.SOUTHEAST)) }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "no active movement selection"
        }

        it("should throw exception when Mech has insufficient movement points") {
            // given: a Mech associated with the moving team and having one movement point
            val mech = strategy.newMech(
                newTestMechSpecification().copy(
                    team = team,
                    type = newTestMechType().copy(movementPoints = 1)
                )
            )
            strategy.deploy(mech, Position(0, 0), Direction.SOUTH)
            // and: the Mech has been selected for movement
            subject.select(mech)

            // when: moving the Mech forward 2 cells
            val operation = { subject.move(Displacement(2, Direction.SOUTH)) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "movement points"
        }
    }

    describe("select") {
        it("should select the specified Mech for movement") {
            // given: a Mech
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))

            // when: selecting the Mech for movement
            subject.select(mech)

            // then: the Mech should be selected
            strategy.selectedMech shouldEqual Option.some(mech)
        }

        it("should throw exception when Mech does not exist") {
            // given: a Mech that was not created by the game
            val mechId = MechId(-1)
            val mech = mock<Mech> {
                on { id } doReturn mechId
                on { this.team } doReturn team
            }

            // when: selecting the Mech for movement
            val operation = { subject.select(mech) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain mechId.toString()
        }

        it("should throw exception when Mech belongs to a different team") {
            // given: a Mech not associated with the moving team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = Team.ATTACKER))

            // when: selecting the Mech for movement
            val operation = { subject.select(mech) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "team"
        }

        it("should throw exception when Mech already selected for movement") {
            // given: a Mech has been selected for movement
            val mech1 = strategy.newMech(newTestMechSpecification().copy(team = team))
            subject.select(mech1)

            // when: selecting a different Mech for movement
            val mech2 = strategy.newMech(newTestMechSpecification().copy(team = team))
            val operation = { subject.select(mech2) }

            // then: it should throw an exception
            operation
                .shouldThrow(IllegalStateException::class)
                .withMessage("cannot begin movement selection for Mech with ID ${mech2.id} when movement selection active for Mech with ID ${mech1.id}")
        }

        it("should throw exception when Mech already has a movement selection for current turn") {
            // given: a Mech that already has a movement selection for the current turn
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
            strategy.addMovementSelection(mech)

            // when: selecting the Mech for movement
            val operation = { subject.select(mech) }

            // then: it should throw an exception
            operation shouldThrow IllegalArgumentException::class withMessage "Mech with ID ${mech.id} has already moved during the current turn"
        }
    }

    describe("turn") {
        it("should turn Mech by specified angle") {
            // given: a Mech associated with the moving team and facing north
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
            strategy.deploy(mech, Position(0, 0), Direction.NORTH)
            // and: the Mech has been selected for movement
            subject.select(mech)

            // when: turning the Mech +2 sextants
            subject.turn(Angle.TWO)

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
            // and: the Mech has been selected for movement
            subject.select(mech)

            // when: turning the Mech +2 sextants
            subject.turn(Angle.TWO)

            // then: it should have four movement points
            strategy.getMech(mech.id).movementPoints shouldEqual 4
        }

        it("should throw exception when no Mech selected for movement") {
            // given: a Mech associated with the moving team
            val mech = strategy.newMech(newTestMechSpecification().copy(team = team))
            strategy.deploy(mech, Position(0, 0), Direction.NORTH)
            // but: no Mech has been selected for movement

            // when: turning the Mech
            val operation = { subject.turn(Angle.TWO) }

            // then: it should throw an exception
            operation shouldThrow IllegalStateException::class withMessage "no active movement selection"
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
            // and: the Mech has been selected for movement
            subject.select(mech)

            // when: turning the Mech +2 sextants
            val operation = { subject.turn(Angle.TWO) }

            // then: it should throw an exception
            val exceptionResult = operation shouldThrow IllegalArgumentException::class
            exceptionResult.exceptionMessage shouldContain "movement points"
        }
    }
}) {
    interface Strategy {
        val game: Game

        val selectedMech: Option<Mech>

        fun addMovementSelection(mech: Mech)

        fun deploy(mech: Mech, position: Position, facing: Direction)

        fun getMech(mechId: MechId): Mech

        fun newMech(mechSpecification: MechSpecification): Mech

        fun setInitiativeWinner(team: Team)
    }
}
