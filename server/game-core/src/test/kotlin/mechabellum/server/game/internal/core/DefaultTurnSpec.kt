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
import mechabellum.server.game.api.core.TurnId
import mechabellum.server.game.api.core.TurnSpec
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object DefaultTurnSpec : Spek({
    describe("constructor") {
        it("should throw exception when initiative history is empty") {
            // when: constructing an instance with an empty initiative history
            val operation = { DefaultTurn(id = TurnId(0), initiativesByTeamHistory = listOf()) }

            // then: it should throw an exception
            operation shouldThrow IllegalArgumentException::class withMessage "expected initiative history to not be empty but was empty"
        }
    }

    describe("initiativeRollsIncomplete") {
        it("should return true when no team has rolled initiative") {
            // given: no team has rolled initiative
            val subject = DefaultTurn(id = TurnId(0), initiativesByTeamHistory = listOf(mapOf()))

            // when: asking if initiative rolls are incomplete
            val initiativeRollsIncomplete = subject.initiativeRollsIncomplete

            // then: it should return true
            initiativeRollsIncomplete shouldEqual true
        }

        it("should return true when one team has rolled initiative") {
            // given: one team has rolled initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(mapOf(Team.ATTACKER to Initiative.MIN))
            )

            // when: asking if initiative rolls are incomplete
            val initiativeRollsIncomplete = subject.initiativeRollsIncomplete

            // then: it should return true
            initiativeRollsIncomplete shouldEqual true
        }

        it("should return false when all teams have rolled initiative") {
            // given: all teams have rolled initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(
                    mapOf(Team.ATTACKER to Initiative.MIN, Team.DEFENDER to Initiative.MIN)
                )
            )

            // when: asking if initiative rolls are incomplete
            val initiativeRollsIncomplete = subject.initiativeRollsIncomplete

            // then: it should return false
            initiativeRollsIncomplete shouldEqual false
        }
    }

    describe("initiativeWinner") {
        it("should return Some when initiative winner present") {
            // given: the attacker wins initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(
                    mapOf(Team.ATTACKER to Initiative.MAX, Team.DEFENDER to Initiative.MIN)
                )
            )

            // when: getting the initiative winner
            val initiativeWinner = subject.initiativeWinner

            // then: it should return the attacker
            initiativeWinner shouldEqual Option.some(Team.ATTACKER)
        }

        it("should return None when no team has rolled initiative") {
            // given: no team has rolled initiative
            val subject = DefaultTurn(id = TurnId(0), initiativesByTeamHistory = listOf(mapOf()))

            // when: getting the initiative winner
            val initiativeWinner = subject.initiativeWinner

            // then: it should return empty
            initiativeWinner shouldEqual Option.none()
        }

        it("should return None when all teams have not rolled initiative") {
            // given: attacker has rolled initiative
            // but: defender has not rolled initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(mapOf(Team.ATTACKER to Initiative.MAX))
            )

            // when: getting the initiative winner
            val initiativeWinner = subject.initiativeWinner

            // then: it should return empty
            initiativeWinner shouldEqual Option.none()
        }

        it("should return None when two teams tied for winning initiative") {
            // given: two teams tied for winning initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(
                    mapOf(Team.ATTACKER to Initiative.MAX, Team.DEFENDER to Initiative.MAX)
                )
            )

            // when: getting the initiative winner
            val initiativeWinner = subject.initiativeWinner

            // then: it should return empty
            initiativeWinner shouldEqual Option.none()
        }
    }

    describe("teamsWithoutInitiative") {
        it("should return all teams when no team has rolled initiative") {
            // given: no team has rolled initiative
            val subject = DefaultTurn(id = TurnId(0), initiativesByTeamHistory = listOf(mapOf()))

            // when: getting teams without initiative
            val teamsWithoutInitiative = subject.teamsWithoutInitiative

            // then: it should return all teams
            teamsWithoutInitiative shouldEqual Team.values().toList()
        }

        it("should return no teams when all teams have rolled initiative") {
            // given: all teams have rolled initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(
                    mapOf(Team.ATTACKER to Initiative.MAX, Team.DEFENDER to Initiative.MIN)
                )
            )

            // when: getting teams without initiative
            val teamsWithoutInitiative = subject.teamsWithoutInitiative

            // then: it should return no teams
            teamsWithoutInitiative.shouldBeEmpty()
        }

        it("should return some teams when subset of teams has not rolled initiative") {
            // given: attacker has rolled initiative
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(mapOf(Team.ATTACKER to Initiative.MAX))
            )

            // when: getting teams without initiative
            val teamsWithoutInitiative = subject.teamsWithoutInitiative

            // then: it should return defender
            teamsWithoutInitiative shouldEqual listOf(Team.DEFENDER)
        }
    }

    describe("setInitiative") {
        it("should set initiative in current iteration when current iteration is incomplete") {
            // given: current initiative iteration is incomplete
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(mapOf(Team.ATTACKER to Initiative.MIN))
            )

            // when: setting initiative
            val newSubject = subject.setInitiative(Team.DEFENDER, Initiative.MAX)

            // then: initiative should be set in current iteration
            newSubject.getInitiative(Team.ATTACKER) shouldEqual Option.some(Initiative.MIN)
            newSubject.getInitiative(Team.DEFENDER) shouldEqual Option.some(Initiative.MAX)
        }

        it("should set initiative in new iteration when current iteration is complete") {
            // given: current initiative iteration is complete
            val subject = DefaultTurn(
                id = TurnId(0),
                initiativesByTeamHistory = listOf(
                    mapOf(Team.ATTACKER to Initiative.MIN, Team.DEFENDER to Initiative.MIN)
                )
            )

            // when: setting initiative
            val newSubject = subject.setInitiative(Team.DEFENDER, Initiative.MAX)

            // then: initiative should be set in new iteration
            newSubject.getInitiative(Team.ATTACKER) shouldEqual Option.none()
            newSubject.getInitiative(Team.DEFENDER) shouldEqual Option.some(Initiative.MAX)
        }
    }
})

object DefaultTurnBehavesAsTurnSpec : TurnSpec(
    newTurn = { initiativesByTeam ->
        DefaultTurn(id = TurnId(0), initiativesByTeamHistory = listOf(initiativesByTeam))
    }
)
