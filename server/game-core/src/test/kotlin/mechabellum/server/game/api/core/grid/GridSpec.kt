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

package mechabellum.server.game.api.core.grid

import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.jetbrains.spek.subject.SubjectSpek

abstract class GridSpec(subjectFactory: (GridSpecification) -> Grid) : SubjectSpek<Grid>({
    val gridSpecification = newTestGridSpecification().copy(type = newTestGridType().copy(cols = 5, rows = 8))

    subject { subjectFactory(gridSpecification) }

    describe("type") {
        it("should return type from grid specification") {
            subject.type shouldEqual gridSpecification.type
        }
    }

    describe("getCell") {
        on(
            "cell present at position %s",
            data(Position(0, 0), expected = Position(0, 0)),
            data(Position(4, 7), expected = Position(4, 7))
        ) { position, expected ->
            it("should return cell at position") {
                subject.getCell(position).position shouldEqual expected
            }
        }

        on(
            "cell not present at position %s",
            data(Position(-1, 0), expected = Unit),
            data(Position(0, -1), expected = Unit),
            data(Position(5, 7), expected = Unit),
            data(Position(4, 8), expected = Unit)
        ) { position, _ ->
            it("should throw exception") {
                // when
                val operation = { subject.getCell(position) }

                // then
                val exceptionResult = operation shouldThrow IllegalArgumentException::class
                exceptionResult.exceptionMessage shouldContain position.toString()
            }
        }
    }

    describe("getDeploymentPositions") {
        it("should return deployment positions from grid specification") {
            Team.values().forEach {
                subject.getDeploymentPositions(it) shouldEqual gridSpecification.deploymentPositionsByTeam[it]
            }
        }
    }
})
