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

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import java.util.ServiceLoader

/** This spec attempts to play an entire game using the training scenario from the Quick-Start rules. */
object TrainingScenarioSpec : Spek({
    it("should be able to play the training scenario from the Quick-Start rules") {
        val gameFactory = ServiceLoader.load(GameFactory::class.java).first()
        @Suppress("UNUSED_VARIABLE")
        val game = gameFactory.newGame()

        val unitTypeRegistry = ServiceLoader.load(UnitTypeRegistry::class.java).first()
        @Suppress("UNUSED_VARIABLE")
        val cicadaMechType = unitTypeRegistry.findMechTypeByName("CDA-2A Cicada").getOrThrow()
        @Suppress("UNUSED_VARIABLE")
        val enforcerMechType = unitTypeRegistry.findMechTypeByName("ENF-4R Enforcer").getOrThrow()
        @Suppress("UNUSED_VARIABLE")
        val hermesIIMechType = unitTypeRegistry.findMechTypeByName("HER-2S Hermes II").getOrThrow()
        @Suppress("UNUSED_VARIABLE")
        val hunchbackMechType = unitTypeRegistry.findMechTypeByName("HBK-4G Hunchback").getOrThrow()

        // TODO: implement remainder of scenario
    }
})
