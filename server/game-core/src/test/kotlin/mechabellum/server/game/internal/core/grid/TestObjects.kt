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

package mechabellum.server.game.internal.core.grid

import mechabellum.server.game.api.core.grid.GridType

internal fun newTestGrid(): InternalGrid = InternalGrid(newTestGridType())

internal fun newTestGridType(cols: Int = 1, rows: Int = 1): GridType = GridType(
    cols = cols,
    name = "Test Grid",
    rows = rows
)
