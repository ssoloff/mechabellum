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

package mechabellum.server.game.api.core.commands.initiative

import mechabellum.server.game.api.core.StatelessCommand
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.InitiativePhase

/** Superclass for stateless commands that are executed during the initiative phase. */
open class StatelessInitiativeCommand<R : Any>(
    action: (InitiativePhase) -> R
) : StatelessCommand<InitiativePhase, R>(InitiativePhase::class, action)

/**
 * Command that rolls initiative for [team].
 *
 * When executed, throws [IllegalStateException] if initiative has been rolled for all teams and a winner has been
 * determined; or if all teams have not yet rolled initiative but initiative has already been rolled for [team].
 */
class RollInitiativeCommand(val team: Team) : StatelessInitiativeCommand<Unit>({
    it.rollInitiative(team)
})
