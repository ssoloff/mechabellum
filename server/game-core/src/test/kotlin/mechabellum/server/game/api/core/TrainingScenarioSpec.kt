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
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.grid.GridSpecification
import mechabellum.server.game.api.core.grid.GridTypeRegistry
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.UnitTypeRegistry
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.util.ServiceLoader

/** This spec attempts to play an entire game using the training scenario from the Quick-Start rules. */
object TrainingScenarioSpec : Spek({
    describe("game implementation") {
        fun newMechSpecification(name: String): MechSpecification {
            val unitTypeRegistry = ServiceLoader.load(UnitTypeRegistry::class.java).first()
            return MechSpecification(
                type = unitTypeRegistry.findMechTypeByName(name).getOrThrow()
            )
        }

        fun newQuickStartGame(): Game {
            val gameFactory = ServiceLoader.load(GameFactory::class.java).first()
            val gridTypeRegistry = ServiceLoader.load(GridTypeRegistry::class.java).first()
            return gameFactory.newGame(
                GameSpecification(
                    gridSpecification = GridSpecification(
                        type = gridTypeRegistry.findByName("Quick-Start Map").getOrThrow()
                    )
                )
            )
        }

        it("should be able to play the training scenario from the Quick-Start rules") {
            val game = newQuickStartGame()

            val defender1 = game.newMech(newMechSpecification("CDA-2A Cicada"))
            val defender2 = game.newMech(newMechSpecification("HBK-4G Hunchback"))
            val attacker1 = game.newMech(newMechSpecification("ENF-4R Enforcer"))
            val attacker2 = game.newMech(newMechSpecification("HER-2S Hermes II"))
            game.endInitialization()

            game.deployMech(defender1, CellId(0, 16))
            game.deployMech(defender2, CellId(14, 16))
            game.deployMech(attacker1, CellId(0, 0))
            game.deployMech(attacker2, CellId(14, 0))

            // TODO: implement remainder of scenario
        }
    }
})
