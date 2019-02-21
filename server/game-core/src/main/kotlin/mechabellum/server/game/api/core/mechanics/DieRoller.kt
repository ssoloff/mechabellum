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

package mechabellum.server.game.api.core.mechanics

import java.util.concurrent.ThreadLocalRandom

/** A six-sided die roller. */
interface DieRoller {
    /** Returns the result of rolling one six-sided die (a value in the range [1,6]). */
    fun roll(): Int

    companion object {
        /** The count of sides on a six-sided die. */
        const val DIE_SIDES: Int = 6
    }
}

/** Implementation of [DieRoller] that randomly generates a die roll using a uniform distribution. */
class UniformDieRoller : DieRoller {
    override fun roll(): Int = ThreadLocalRandom.current().nextInt(DieRoller.DIE_SIDES) + 1
}
