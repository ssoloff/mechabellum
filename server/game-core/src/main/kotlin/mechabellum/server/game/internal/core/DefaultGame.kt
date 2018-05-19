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

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.grid.Position
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.phases.InitiativePhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.grid.DefaultGrid
import mechabellum.server.game.internal.core.unit.DefaultMech

internal class DefaultGame(override val grid: DefaultGrid) : Game {
    private val mechRecordsById: MutableMap<MechId, MechRecord> = hashMapOf()
    private var nextMechId: Int = 0

    override var phase: Phase = DefaultInitializationPhase()
        private set

    fun getMech(id: MechId): DefaultMech = getMechRecord(id).mech

    fun getMechPosition(id: MechId): Option<Position> = getMechRecord(id).position

    private fun getMechRecord(id: MechId): MechRecord =
        mechRecordsById[id] ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    private class MechRecord(
        val mech: DefaultMech,
        var position: Option<Position> = Option.none()
    )

    abstract inner class DefaultPhase : Phase {
        override val game: DefaultGame = this@DefaultGame
    }

    inner class DefaultDeploymentPhase(override val team: Team) : DefaultPhase(), DeploymentPhase {
        override fun deployMech(mech: Mech, position: Position) {
            val mechRecord = getMechRecord(mech.id)
            checkMechBelongsToDeployingTeam(mech)
            checkPositionIsWithinTeamDeploymentPositions(position, mech.team)
            mechRecord.position = Option.some(position)
        }

        private fun checkMechBelongsToDeployingTeam(mech: Mech) {
            require(mech.team == team) { "only team $team may deploy during this phase" }
        }

        private fun checkPositionIsWithinTeamDeploymentPositions(position: Position, team: Team) {
            val deploymentPositions = game.grid.getDeploymentPositions(team)
            require(position in deploymentPositions) {
                "position $position is not in deployment positions $deploymentPositions for team $team"
            }
        }

        override fun end() {
            checkAllTeamMechsDeployed()

            phase = when (team) {
                Team.DEFENDER -> DefaultDeploymentPhase(Team.ATTACKER)
                Team.ATTACKER -> DefaultInitiativePhase()
            }
        }

        private fun checkAllTeamMechsDeployed() {
            mechRecordsById.values
                .filter { it.mech.team == team }
                .find { it.position is Option.None }
                ?.let { throw GameException(Messages.mechHasNotBeenDeployed(it.mech)) }
        }
    }

    inner class DefaultInitializationPhase : DefaultPhase(), InitializationPhase {
        override fun end() {
            checkAllTeamsHaveAtLeastOneMech()

            phase = DefaultDeploymentPhase(Team.DEFENDER)
        }

        private fun checkAllTeamsHaveAtLeastOneMech() {
            for (team in Team.values()) {
                if (mechRecordsById.values.none { it.mech.team == team }) {
                    throw GameException(Messages.teamHasNoMechs(team))
                }
            }
        }

        override fun newMech(specification: MechSpecification): Mech {
            val mech = DefaultMech(
                id = MechId(nextMechId++),
                team = specification.team
            )
            assert(mech.id !in mechRecordsById)
            mechRecordsById[mech.id] = MechRecord(mech)
            return mech
        }
    }

    inner class DefaultInitiativePhase : DefaultPhase(), InitiativePhase {
        override fun end() {
            TODO("not implemented")
        }
    }

    private interface Messages {
        fun mechHasNotBeenDeployed(mechId: MechId): String
        fun teamHasNoMechs(teamName: String): String

        companion object : Messages by DefaultMessageFactory.get(Messages::class)
    }

    private fun Messages.mechHasNotBeenDeployed(mech: DefaultMech): String = mechHasNotBeenDeployed(mech.id)
    private fun Messages.teamHasNoMechs(team: Team): String = teamHasNoMechs(team.name.toLowerCase())
}
