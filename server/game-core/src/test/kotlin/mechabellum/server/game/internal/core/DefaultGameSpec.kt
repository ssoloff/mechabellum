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
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.api.core.phases.DeploymentPhaseSpec
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.phases.InitializationPhaseSpec
import mechabellum.server.game.internal.core.grid.DefaultGrid

object DefaultGameBehavesAsCommandContextSpec : CommandContextSpec(
    activePhaseType = InitializationPhase::class,
    subjectFactory = { DefaultGame(DefaultGrid(newTestGridSpecification())) }
)

object DefaultGameBehavesAsGameSpec : GameSpec({ DefaultGame(DefaultGrid(newTestGridSpecification())) })

object DefaultGameBehavesAsDeploymentPhaseSpec : DeploymentPhaseSpec(
    getMechPosition = { mechId -> (this as DefaultGame.DefaultPhase).game.getMechPosition(mechId).getOrThrow() },
    newMech = { mechSpecification ->
        (this as DefaultGame.DefaultPhase).game.DefaultInitializationPhase().newMech(mechSpecification)
    },
    subjectFactory = { gridSpecification -> DefaultGame(DefaultGrid(gridSpecification)).DefaultDeploymentPhase() }
)

object DefaultGameBehavesAsInitializationPhaseSpec : InitializationPhaseSpec(
    getActivePhase = { (this as DefaultGame.DefaultPhase).game.phase },
    getMech = { mechId -> (this as DefaultGame.DefaultPhase).game.getMech(mechId) },
    subjectFactory = { DefaultGame(DefaultGrid(newTestGridSpecification())).DefaultInitializationPhase() }
)
