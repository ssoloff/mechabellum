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

package mechabellum.server.game.api.core.commands.initialization

import mechabellum.server.game.api.core.CommandResult
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.StatelessCommand
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechSpecification

/** Superclass for stateless commands that are executed during the initialization phase. */
open class StatelessInitializationCommand<R : Any>(
    action: (InitializationPhase) -> R
) : StatelessCommand<R, InitializationPhase>(InitializationPhase::class, action)

/** Ends the initialization phase. Returns failure if all teams do not have at least one Mech. */
fun Game.endInitialization(): CommandResult<Unit> = executeCommand(EndInitializationCommand())

class EndInitializationCommand : StatelessInitializationCommand<Unit>({
    it.end()
})

/** Returns a new Mech based on [specification]. */
fun Game.newMech(specification: MechSpecification): CommandResult<Mech> =
    executeCommand(NewMechCommand(specification))

class NewMechCommand(specification: MechSpecification) : StatelessInitializationCommand<Mech>({
    it.newMech(specification)
})
