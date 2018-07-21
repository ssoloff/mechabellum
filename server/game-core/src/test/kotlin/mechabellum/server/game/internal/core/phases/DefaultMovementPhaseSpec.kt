// ktlint-disable filename

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

package mechabellum.server.game.internal.core.phases

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.grid.Direction
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.MovementPhaseSpec
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultTurn
import mechabellum.server.game.internal.core.mechanics.InitiativeHistory
import mechabellum.server.game.internal.core.mechanics.MovementSelection

object DefaultMovementPhaseBehavesAsMovementPhaseSpec : MovementPhaseSpec(
    newStrategy = { gameSpecification ->
        object : Strategy {
            override val game: DefaultGame = DefaultGame(gameSpecification)

            init {
                game.state.addTurn()
            }

            override val selectedMech: Option<Mech>
                get() = game.turn.movementSelection.map { game.state.getMech(it.mechId) }

            override fun addMovementSelection(mech: Mech) {
                game.state.modifyTurn(game.turn.id) { turn ->
                    DefaultTurn(
                        id = turn.id,
                        initiativeHistory = turn.initiativeHistory,
                        movementSelection = turn.movementSelection,
                        movementSelections = turn.movementSelections + MovementSelection(mech.id)
                    )
                }
            }

            override fun deploy(mech: Mech, position: Position, facing: Direction) =
                DefaultDeploymentPhase(game, mech.team).deploy(mech, position, facing)

            override fun getMech(mechId: MechId): Mech = game.state.getMech(mechId)

            override fun newMech(mechSpecification: MechSpecification): Mech =
                DefaultInitializationPhase(game).newMech(mechSpecification)

            override fun setInitiativeWinner(team: Team) {
                game.state.modifyTurn(game.turn.id) { turn ->
                    turn.setInitiativeHistory(
                        InitiativeHistory()
                            .setInitiative(team, Initiative.MAX)
                            .setInitiative(team.opponent, Initiative.MIN)
                    )
                }
            }
        }
    },
    newSubject = { strategy, team ->
        val game = strategy.game as DefaultGame
        DefaultMovementPhase(game = game, team = team, turnId = game.turn.id)
    }
)
