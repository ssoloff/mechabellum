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
import mechabellum.server.game.api.core.features.DeploymentFeature
import mechabellum.server.game.api.core.features.DeploymentFeatureSpec
import mechabellum.server.game.api.core.features.GridFeatureSpec
import mechabellum.server.game.api.core.grid.newTestGridType
import mechabellum.server.game.internal.core.grid.InternalGrid

object InternalGameBehavesAsCommandContextSpec : CommandContextSpec(
    presentFeatureType = DeploymentFeature::class.java,
    subjectFactory = { InternalGame(InternalGrid(newTestGridType())) }
)

object InternalGameBehavesAsDeploymentFeatureSpec : DeploymentFeatureSpec(
    getMech = { subject, mechId -> (subject as InternalGame).getMech(mechId) },
    getMechPosition = { subject, mechId -> (subject as InternalGame).getMechPosition(mechId) },
    subjectFactory = { gridSpecification -> InternalGame(InternalGrid(gridSpecification.type)) }
)

object InternalGameBehavesAsGameSpec : GameSpec({ InternalGame(InternalGrid(newTestGridType())) })

object InternalGameBehavesAsGridFeatureSpec : GridFeatureSpec(
    { gridSpecification -> InternalGame(InternalGrid(gridSpecification.type)) }
)
