/*
 * Copyright (C) 2019 Mechabellum contributors
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

package mechabellum.server.game.internal.core.phases

import mechabellum.server.game.api.core.GameSpecification
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.AttackerDeploymentPhaseSpec
import mechabellum.server.game.api.core.phases.CommonDeploymentPhaseSpec
import mechabellum.server.game.api.core.phases.DefenderDeploymentPhaseSpec
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.phases.DeploymentPhaseSpec
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.DefaultGame

object DefaultDeploymentPhaseSpecs {
    fun newStrategy(gameSpecification: GameSpecification): DeploymentPhaseSpec.Strategy {
        return object : DeploymentPhaseSpec.Strategy {
            override val game: DefaultGame = DefaultGame(gameSpecification)

            override fun deploy(mech: Mech, position: Position, facing: Direction) =
                DefaultDeploymentPhase(game, mech.team).deploy(mech, position, facing)

            override fun getMech(mechId: MechId): Mech = game.state.getMech(mechId)

            override fun newMech(mechSpecification: MechSpecification): Mech =
                DefaultInitializationPhase(game).newMech(mechSpecification)
        }
    }

    fun newSubject(strategy: DeploymentPhaseSpec.Strategy, team: Team): DeploymentPhase =
        DefaultDeploymentPhase(strategy.game as DefaultGame, team)
}

object DefaultDeploymentPhaseBehavesAsCommonDeploymentPhaseSpec : CommonDeploymentPhaseSpec(
    newStrategy = DefaultDeploymentPhaseSpecs::newStrategy,
    newSubject = DefaultDeploymentPhaseSpecs::newSubject
)

object DefaultDeploymentPhaseBehavesAsAttackerDeploymentPhaseSpec : AttackerDeploymentPhaseSpec(
    newStrategy = DefaultDeploymentPhaseSpecs::newStrategy,
    newSubject = DefaultDeploymentPhaseSpecs::newSubject
)

object DefaultDeploymentPhaseBehavesAsDefenderDeploymentPhaseSpec : DefenderDeploymentPhaseSpec(
    newStrategy = DefaultDeploymentPhaseSpecs::newStrategy,
    newSubject = DefaultDeploymentPhaseSpecs::newSubject
)
