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

package mechabellum.server.game.api.core

import java.util.ArrayDeque
import java.util.Queue

internal class ScriptedDieRoller : DieRoller {
    private val values: Queue<Int> = ArrayDeque()

    fun addValues(vararg values: Int) {
        require(values.all { it in 1..DieRoller.DIE_SIDES }) { "value out of range" }

        this.values.addAll(values.toTypedArray())
    }

    override fun roll(): Int = values.poll() ?: throw IllegalStateException("no die roll value available")
}
