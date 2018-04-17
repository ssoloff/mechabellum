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

package mechabellum.server.game.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

abstract class AbstractGridTestCase {
    abstract fun createGrid(cols: Int, rows: Int): Grid

    @Nested
    inner class GetCell {
        @Test
        fun `should return requested cell when cell exists`() {
            val grid = createGrid(5, 8)

            assertEquals(CellId(0, 0), grid.getCell(0, 0).id)
            assertEquals(CellId(4, 7), grid.getCell(4, 7).id)
        }

        @Test
        fun `should throw exception when cell does not exist`() {
            val grid = createGrid(5, 8)

            assertThrows<IllegalArgumentException> { grid.getCell(-1, 0) }
            assertThrows<IllegalArgumentException> { grid.getCell(0, -1) }
            assertThrows<IllegalArgumentException> { grid.getCell(5, 7) }
            assertThrows<IllegalArgumentException> { grid.getCell(4, 8) }
        }
    }
}
