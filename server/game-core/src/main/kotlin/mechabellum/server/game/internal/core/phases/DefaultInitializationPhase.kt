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

import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.DefaultGame
import mechabellum.server.game.internal.core.DefaultGameState
import mechabellum.server.game.internal.core.DefaultMessageFactory
import mechabellum.server.game.internal.core.DefaultPhase
import mechabellum.server.game.internal.core.unit.DefaultMech

internal class DefaultInitializationPhase(game: DefaultGame) : DefaultPhase(game), InitializationPhase {
    override fun end() {
        checkAllTeamsHaveAtLeastOneMech()

        game.phase = DefaultDeploymentPhase(game, Team.DEFENDER)
    }

    private fun checkAllTeamsHaveAtLeastOneMech() {
        for (team in Team.values()) {
            if (game.state.mechRecords.none { it.mech.team == team }) {
                throw GameException(Messages.teamHasNoMechs(team))
            }
        }
    }

    override fun newMech(specification: MechSpecification): Mech {
        val mech = DefaultMech(
            id = game.state.newMechId(),
            team = specification.team
        )
        game.state.addMechRecord(DefaultGameState.MechRecord(mech))
        return mech
    }

    private interface Messages {
        fun teamHasNoMechs(teamName: String): String

        companion object : Messages by DefaultMessageFactory.get(Messages::class)
    }

    private fun Messages.teamHasNoMechs(team: Team): String = teamHasNoMechs(team.name.toLowerCase())
}
