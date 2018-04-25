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
import mechabellum.server.game.api.core.grid.CellId
import mechabellum.server.game.api.core.unit.Mech
import mechabellum.server.game.api.core.unit.MechId

internal class InternalGame : CommandContext, DeploymentFeature, Game {
    private val mechDatasById: MutableMap<MechId, MechData> = hashMapOf()

    override fun deployMech(mech: Mech, position: CellId) {
        // TODO: we need to enforce that [mech] is of type InternalMech
        require(mech.id !in mechDatasById, { "Mech with ID ${mech.id} already present" })

        mechDatasById[mech.id] = MechData(mech, position)
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
            else -> Option.none()
        }
    }

    // TODO: change to return InternalMech
    fun getMech(id: MechId): Mech = mechDatasById[id]?.mech ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    fun getMechPosition(id: MechId): CellId =
        mechDatasById[id]?.position ?: throw IllegalArgumentException("unknown Mech ID ($id)")

    private class MechData(val mech: Mech, val position: CellId)
}
