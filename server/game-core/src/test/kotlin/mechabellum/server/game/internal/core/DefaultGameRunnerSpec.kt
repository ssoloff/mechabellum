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

import mechabellum.server.game.api.core.GameRunnerFactorySpec
import mechabellum.server.game.api.core.GameRunnerSpec
import mechabellum.server.game.api.core.grid.newTestGridSpecification
import mechabellum.server.game.internal.core.grid.DefaultGrid

object DefaultGameRunnerBehavesAsGameRunnerSpec : GameRunnerSpec(
    subjectFactory = { DefaultGameRunner(DefaultGame(DefaultGrid(newTestGridSpecification()))) }
)

object DefaultGameRunnerFactoryBehavesAsGameRunnerFactorySpec : GameRunnerFactorySpec(::DefaultGameRunnerFactory)
