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

package mechabellum.server.game.internal.core.unit

import mechabellum.server.common.api.core.util.Option
import mechabellum.server.game.api.core.unit.MechType
import mechabellum.server.game.api.core.unit.UnitTypeRegistry

internal class InternalUnitTypeRegistry : UnitTypeRegistry {
    private val _mechTypes: Collection<MechType> = listOf(
        MechType("CDA-2A Cicada"),
        MechType("ENF-4R Enforcer"),
        MechType("HER-2S Hermes II"),
        MechType("HBK-4G Hunchback")
    )

    override fun findMechTypeByName(name: String): Option<MechType> = Option.of(_mechTypes.find { it.name == name })
}