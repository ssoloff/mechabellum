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
import mechabellum.server.game.api.core.CommandContext
import mechabellum.server.game.api.core.Game
import mechabellum.server.game.api.core.GameException
import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.grid.Grid
import mechabellum.server.game.api.core.phases.DeploymentPhase
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.grid.InternalGrid
import mechabellum.server.game.internal.core.unit.InternalMech

internal class InternalGame(val grid: InternalGrid) : CommandContext, Game {
    private val _mechRecordsById: MutableMap<MechId, MechRecord> = hashMapOf()
    private var _nextMechId: Int = 0
    private var _phase: Phase = InternalInitializationPhase()

    override fun <T : Any> executeCommand(command: Command<T>): T {
        try {
            return command.execute(this)
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Exception) {
            // TODO: i18n
            throw GameException("failed to execute game command", e)
        }
    }

    override fun getActivePhase(): Phase = _phase

    @Suppress("UNCHECKED_CAST")
    override fun <T : Phase> getActivePhaseAs(type: Class<T>): Option<T> =
        if (type.isInstance(_phase)) Option.some(_phase as T) else Option.none()

    fun getMech(id: MechId): InternalMech =
        _mechRecordsById[id]?.mech ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    fun getMechPosition(id: MechId): Option<CellId> =
        _mechRecordsById[id]?.position ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    private class MechRecord(
        val mech: InternalMech,
        var position: Option<CellId> = Option.none()
    )

    open inner class InternalPhase : Phase {
        val game: InternalGame = this@InternalGame

        override val grid: Grid
            get() = game.grid
    }

    inner class InternalDeploymentPhase : InternalPhase(), DeploymentPhase {
        override fun deployMech(mech: Mech, position: CellId) {
            require((position.col in 0 until grid.type.cols) && (position.row in 0 until grid.type.rows)) {
                "position $position does not exist in grid of type ${grid.type}"
            }

            val mechRecord = _mechRecordsById[mech.id] ?: throw IllegalArgumentException("unknown Mech ID (${mech.id})")
            mechRecord.position = Option.some(position)
        }
    }

    inner class InternalInitializationPhase : InternalPhase(), InitializationPhase {
        override fun end() {
            if (_mechRecordsById.isEmpty()) {
                // TODO: i18n
                throw GameException("no Mechs added during initialization")
            }
            _phase = InternalDeploymentPhase()
        }

        override fun newMech(specification: MechSpecification): Mech {
            val mech = InternalMech(MechId(_nextMechId++))
            assert(mech.id !in _mechRecordsById)
            _mechRecordsById[mech.id] = MechRecord(mech)
            return mech
        }
    }
}
