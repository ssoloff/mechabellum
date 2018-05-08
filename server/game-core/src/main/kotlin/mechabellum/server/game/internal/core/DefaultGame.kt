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
import mechabellum.server.game.api.core.Command
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.grid.Grid
import mechabellum.server.game.api.core.participant.Team
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.grid.DefaultGrid
import mechabellum.server.game.internal.core.unit.DefaultMech
import kotlin.reflect.full.cast

internal class DefaultGame(val grid: DefaultGrid) : Game {
    private val mechRecordsById: MutableMap<MechId, MechRecord> = hashMapOf()
    private var nextMechId: Int = 0

    var phase: Phase = DefaultInitializationPhase()
        private set

    override fun <R : Any, TPhase : Phase> executeCommand(command: Command<R, TPhase>): R {
        require(command.phaseType.isInstance(phase)) { "phase not active (${command.phaseType.simpleName}" }

        try {
            return command.execute(command.phaseType.cast(phase))
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Exception) {
            throw GameException(Messages.commandExecutionFailure, e)
        }
    }

    fun getMech(id: MechId): DefaultMech =
        mechRecordsById[id]?.mech ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    fun getMechPosition(id: MechId): Option<CellId> =
        mechRecordsById[id]?.position ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    private class MechRecord(
        val mech: DefaultMech,
        var position: Option<CellId> = Option.none()
    )

    open inner class DefaultPhase : Phase {
        val game: DefaultGame = this@DefaultGame

        override val grid: Grid
            get() = game.grid
    }

    inner class DefaultDeploymentPhase : DefaultPhase(), DeploymentPhase {
        override fun deployMech(mech: Mech, position: CellId) {
            val mechRecord = mechRecordsById[mech.id] ?: throw IllegalArgumentException("unknown Mech ID (${mech.id})")
            checkPositionIsWithinTeamDeploymentZone(position, mech.team)
            mechRecord.position = Option.some(position)
        }

        private fun checkPositionIsWithinTeamDeploymentZone(position: CellId, team: Team) {
            val deploymentZone = grid.getDeploymentZone(team)
            require((position.col in deploymentZone.colRange) && (position.row in deploymentZone.rowRange)) {
                "position $position is not in deployment zone $deploymentZone for team $team"
            }
        }
    }

    inner class DefaultInitializationPhase : DefaultPhase(), InitializationPhase {
        override fun end() {
            checkAllTeamsHaveAtLeastOneMech()

            phase = DefaultDeploymentPhase()
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

    private interface Messages {
        val commandExecutionFailure: String

        fun teamHasNoMechs(teamName: String): String

        companion object : Messages by DefaultMessageFactory.get(Messages::class)
    }

    private fun Messages.teamHasNoMechs(team: Team) = teamHasNoMechs(team.name.toLowerCase())
}
