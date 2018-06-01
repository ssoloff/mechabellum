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

import mechabellum.server.game.api.core.participant.Team

/**
 * A game turn.
 *
 * Each turn encapsulates information about one instance of each of the following phases:
 *
 * - Initiative phase
 * - Movement phase per team
 * - Weapon attack phase per team
 * - End phase
 */
interface Turn {
    /** Returns the initiative roll for the specified [team] (a value in the range [2,12]). */
    fun getInitiativeRoll(team: Team): Int
}
