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
import mechabellum.server.game.api.core.grid.Grid
import mechabellum.server.game.api.core.phases.InitializationPhase
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.grid.InternalGrid
import mechabellum.server.game.internal.core.unit.InternalMech

internal class InternalGame(val grid: InternalGrid) : CommandContext, Game {
    private val _mechsById: MutableMap<MechId, InternalMech> = hashMapOf()
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

    fun getMech(id: MechId): InternalMech = _mechsById[id] ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    open inner class InternalPhase : Phase {
        override val grid: Grid
            get() = this@InternalGame.grid
    }

    inner class InternalInitializationPhase : InternalPhase(), InitializationPhase {
        val game: InternalGame = this@InternalGame

        override fun newMech(specification: MechSpecification): Mech {
            val mech = InternalMech(MechId(_nextMechId++))
            _mechsById[mech.id] = mech
            return mech
        }
    }
}
