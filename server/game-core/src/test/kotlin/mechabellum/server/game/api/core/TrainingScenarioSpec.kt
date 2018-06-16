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

import mechabellum.server.game.api.core.commands.deployment.DeployCommand
import mechabellum.server.game.api.core.commands.general.EndPhaseCommand
import mechabellum.server.game.api.core.commands.initialization.NewMechCommand
import mechabellum.server.game.api.core.commands.initiative.RollInitiativeCommand
import mechabellum.server.game.api.core.commands.movement.TurnCommand
import mechabellum.server.game.api.core.grid.Angle
import mechabellum.server.game.api.core.grid.Direction
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
        val dieRoller = ScriptedDieRoller()
        val gameRunner = DomainObjects.newGameRunner(dieRoller)

        fun <TPhase : Phase, R : Any> executeCommand(command: Command<TPhase, R>): R =
            gameRunner.executeCommand(command)

        it("should be able to play the training scenario from the Quick-Start rules") {
            // initialization phase
            val defender1 = executeCommand(NewMechCommand(MechSpecification(Team.DEFENDER, MechTypes.CICADA)))
            val defender2 = executeCommand(NewMechCommand(MechSpecification(Team.DEFENDER, MechTypes.HUNCHBACK)))
            val attacker1 = executeCommand(NewMechCommand(MechSpecification(Team.ATTACKER, MechTypes.ENFORCER)))
            val attacker2 = executeCommand(NewMechCommand(MechSpecification(Team.ATTACKER, MechTypes.HERMES_II)))
            executeCommand(EndPhaseCommand())

            // defender deployment phase
            executeCommand(DeployCommand(defender1, Position(0, 16), Direction.NORTH))
            executeCommand(DeployCommand(defender2, Position(14, 16), Direction.NORTH))
            executeCommand(EndPhaseCommand())

            // attacker deployment phase
            executeCommand(DeployCommand(attacker1, Position(0, 0), Direction.SOUTH))
            executeCommand(DeployCommand(attacker2, Position(14, 0), Direction.SOUTH))
            executeCommand(EndPhaseCommand())

            // turn 0

            // initiative phase
            dieRoller.addValues(6, 6) // attacker initiative = 12 (winner)
            executeCommand(RollInitiativeCommand(Team.ATTACKER))
            dieRoller.addValues(1, 1) // defender initiative = 2 (loser)
            executeCommand(RollInitiativeCommand(Team.DEFENDER))
            executeCommand(EndPhaseCommand())

            // attacker movement phase
            executeCommand(TurnCommand(attacker1, -Angle.ONE))
            executeCommand(TurnCommand(attacker2, Angle.ONE))
            // TODO: implement end() in order to move to defender movement phase

            // TODO: implement remainder of scenario
        }
    }
}) {
    private object DomainObjects {
        fun newGameRunner(dieRoller: DieRoller): GameRunner {
            val gameRunnerFactory = ServiceLoader.load(GameRunnerFactory::class.java).first()
            return gameRunnerFactory.newGameRunner(
                GameSpecification(
                    dieRoller = dieRoller,
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
    }

    private object MechTypes {
        val CICADA = MechType("CDA-2A Cicada")
        val ENFORCER = MechType("ENF-4R Enforcer")
        val HERMES_II = MechType("HER-2S Hermes II")
        val HUNCHBACK = MechType("HBK-4G Hunchback")
    }
}
