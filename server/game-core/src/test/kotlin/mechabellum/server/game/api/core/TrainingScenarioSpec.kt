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

import mechabellum.server.game.api.core.commands.deployment.deployMech
import mechabellum.server.game.api.core.commands.initialization.endInitialization
import mechabellum.server.game.api.core.commands.initialization.newMech
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.GridType
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.MechType
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.util.ServiceLoader

/** This spec attempts to play an entire game using the training scenario from the Quick-Start rules. */
object TrainingScenarioSpec : Spek({
    describe("game implementation") {
        fun newMechSpecification(type: MechType, team: Team): MechSpecification {
            return MechSpecification(
                team = team,
                type = type
            )
        }

        fun newQuickStartGame(): Game {
            val gameFactory = ServiceLoader.load(GameFactory::class.java).first()
            return gameFactory.newGame(
                GameSpecification(
                    gridSpecification = GridSpecification(
                        deploymentPositionsByTeam = mapOf(
                            Team.ATTACKER to Position(0, 0)..Position(14, 0),
                            Team.DEFENDER to Position(0, 14)..Position(14, 16)
                        ),
                        type = GridType(cols = 15, name = "Quick-Start Map", rows = 17)
                    )
                )
            )
        }

        it("should be able to play the training scenario from the Quick-Start rules") {
            val game = newQuickStartGame()

            val defender1 = game.newMech(newMechSpecification(MechTypes.CICADA, Team.DEFENDER))
            val defender2 = game.newMech(newMechSpecification(MechTypes.HUNCHBACK, Team.DEFENDER))
            val attacker1 = game.newMech(newMechSpecification(MechTypes.ENFORCER, Team.ATTACKER))
            val attacker2 = game.newMech(newMechSpecification(MechTypes.HERMES_II, Team.ATTACKER))
            game.endInitialization()

            game.deployMech(defender1, Position(0, 16))
            game.deployMech(defender2, Position(14, 16))
            game.deployMech(attacker1, Position(0, 0))
            game.deployMech(attacker2, Position(14, 0))

            // TODO: implement remainder of scenario
        }
    }
}) {
    private object MechTypes {
        val CICADA = MechType("CDA-2A Cicada")
        val ENFORCER = MechType("ENF-4R Enforcer")
        val HERMES_II = MechType("HER-2S Hermes II")
        val HUNCHBACK = MechType("HBK-4G Hunchback")
    }
}
