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

package mechabellum.server.game.api.core.grid

import mechabellum.server.common.api.test.DataClassSpec
import mechabellum.server.common.api.test.ranges.ClosedRangeSpec

object CellRangeBehavesAsClosedRangeSpec : ClosedRangeSpec<CellId>(
    inRangeValue = CellId(5, 5),
    newEmptyInstance = { CellRange(CellId(0, 0), CellId(-1, -1)) },
    newNonEmptyInstance = { CellRange(CellId(0, 0), CellId(10, 10)) },
    outOfRangeValue = CellId(11, 11)
)

object CellRangeBehavesAsDataClassSpec : DataClassSpec({ CellRange(CellId(0, 0), CellId(5, 8)) })
