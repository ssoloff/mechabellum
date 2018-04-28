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
import mechabellum.server.game.api.core.features.DeploymentFeature
import mechabellum.server.game.api.core.features.GridFeature
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId
import mechabellum.server.game.api.core.unit.MechSpecification
import mechabellum.server.game.internal.core.grid.InternalGrid
import mechabellum.server.game.internal.core.unit.InternalMech

internal class InternalGame(override val grid: InternalGrid) : CommandContext, DeploymentFeature, Game, GridFeature {
    private val _mechDatasById: MutableMap<MechId, MechData> = hashMapOf()
    private var _nextMechId: Int = 0

    override fun deployMech(specification: MechSpecification, position: CellId): Mech {
        val mech = InternalMech(MechId(_nextMechId++))
        _mechDatasById[mech.id] = MechData(mech, position)
        return mech
    }

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

    override fun <T : Any> getFeature(type: Class<T>): Option<T> {
        @Suppress("UNCHECKED_CAST")
        return when (type) {
            DeploymentFeature::class.java -> Option.some(this as T)
            GridFeature::class.java -> Option.some(this as T)
            else -> Option.none()
        }
    }

    fun getMech(id: MechId): InternalMech =
        _mechDatasById[id]?.mech ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    fun getMechPosition(id: MechId): CellId =
        _mechDatasById[id]?.position ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    private class MechData(val mech: InternalMech, val position: CellId)
}
