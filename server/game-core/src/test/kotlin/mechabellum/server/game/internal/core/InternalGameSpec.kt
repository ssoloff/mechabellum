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

import mechabellum.server.game.api.core.CommandContextSpec
import mechabellum.server.game.api.core.GameSpec
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.phases.InitializationPhaseSpec
import mechabellum.server.game.internal.core.grid.InternalGrid

object InternalGameBehavesAsCommandContextSpec : CommandContextSpec(
    activePhaseType = InitializationPhase::class.java,
    subjectFactory = { InternalGame(InternalGrid(newTestGridType())) }
)

object InternalGameBehavesAsGameSpec : GameSpec({ InternalGame(InternalGrid(newTestGridType())) })

object InternalGameBehavesAsInitializationPhaseSpec : InitializationPhaseSpec(
    getMech = { subject, mechId -> (subject as InternalGame.InternalInitializationPhase).game.getMech(mechId) },
    subjectFactory = { InternalGame(InternalGrid(newTestGridType())).InternalInitializationPhase() }
)
