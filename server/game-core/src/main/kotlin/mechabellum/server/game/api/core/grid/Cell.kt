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

/**
 * The unique identifier of a cell in a hexagonal grid.
 *
 * @property col The cell column.
 * @property row The cell row.
 */
data class CellId(val col: Int, val row: Int) : Comparable<CellId> {
    override fun compareTo(other: CellId): Int = COMPARATOR.compare(this, other)

    /** Creates a range from this value to the specified [other] value. */
    operator fun rangeTo(other: CellId): CellIdRange = CellIdRange(this, other)

    companion object {
        private val COMPARATOR = Comparator.comparingInt(CellId::col).thenComparingInt(CellId::row)
    }
}

/** Returns a range from this value up to but excluding the specified [to] value. */
infix fun CellId.until(to: CellId): CellIdRange = CellIdRange(this, CellId(to.col - 1, to.row - 1))

/**
 * A rectangular range of cell identifiers.
 *
 * @property start The minimum value in the range.
 * @property endInclusive The maximum value in the range (inclusive).
 */
data class CellIdRange(override val start: CellId, override val endInclusive: CellId) : ClosedRange<CellId>

/** A cell in a hexagonal grid. */
interface Cell {
    /** The cell identifier. */
    val id: CellId
}
