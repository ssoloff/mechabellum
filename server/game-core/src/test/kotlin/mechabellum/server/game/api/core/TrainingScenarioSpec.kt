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

import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.api.core.unit.UnitTypeRegistry
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.util.ServiceLoader

/** This spec attempts to play an entire game using the training scenario from the Quick-Start rules. */
object TrainingScenarioSpec : Spek({
    describe("game implementation") {
        val gameFactory = ServiceLoader.load(GameFactory::class.java).first()
        val unitTypeRegistry = ServiceLoader.load(UnitTypeRegistry::class.java).first()

        fun newMech(name: String): Mech = gameFactory.newMech(
            MechSpecification(
                type = unitTypeRegistry.findMechTypeByName(name).getOrThrow()
            )
        )

        it("should be able to play the training scenario from the Quick-Start rules") {
            @Suppress("UNUSED_VARIABLE")
            val game = gameFactory.newGame()

            @Suppress("UNUSED_VARIABLE")
            val cicadaMech = newMech("CDA-2A Cicada")
            @Suppress("UNUSED_VARIABLE")
            val enforcerMech = newMech("ENF-4R Enforcer")
            @Suppress("UNUSED_VARIABLE")
            val hermesIIMech = newMech("HER-2S Hermes II")
            @Suppress("UNUSED_VARIABLE")
            val hunchbackMech = newMech("HBK-4G Hunchback")

            // TODO: implement remainder of scenario
        }
    }
})
