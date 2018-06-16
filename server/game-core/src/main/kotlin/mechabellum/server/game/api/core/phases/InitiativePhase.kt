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

package mechabellum.server.game.api.core.phases

import mechabellum.server.game.api.core.Phase
import mechabellum.server.game.api.core.mechanics.Initiative
import mechabellum.server.game.api.core.participant.Team

/** The first phase within a turn in which each team's initiative is determined for the remainder of the turn. */
interface InitiativePhase : Phase {
    /**
     * @throws IllegalStateException If initiative has been rolled for all teams but there is no winner (e.g. if there
     * is a tie for the highest roll); or if all teams have not yet rolled initiative in the latest iteration.
     */
    override fun end()

    /**
     * Rolls initiative for [team] and returns the result.
     *
     * All teams must roll for initiative. If, after all teams have rolled initiative, there is a winner (e.g. no ties
     * for the highest roll), the initiative phase is complete and no further initiative rolls can be made. If, after
     * all teams have rolled initiative, there is no winner, then each team must roll again. The results of each
     * iteration are retained for purposes of review.
     *
     * @throws IllegalStateException If initiative has been rolled for all teams and a winner has been determined; or
     * if all teams have not yet rolled initiative but initiative has already been rolled for [team].
     */
    fun rollInitiative(team: Team): Initiative
}
