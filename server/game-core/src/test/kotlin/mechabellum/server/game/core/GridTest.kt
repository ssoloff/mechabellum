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

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GridTest {
    @Nested
    inner class NewInstance {
        @Test
        fun `should throw exception when cols not positive`() {
            assertThrows<IllegalArgumentException> { Grid.newInstance(0, 1) }
        }

        @Test
        fun `should throw exception when rows not positive`() {
            assertThrows<IllegalArgumentException> { Grid.newInstance(1, 0) }
        }
    }
}
