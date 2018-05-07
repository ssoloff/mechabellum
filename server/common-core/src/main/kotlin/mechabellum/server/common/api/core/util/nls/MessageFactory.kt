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

package mechabellum.server.common.api.core.util.nls

import com.github.rodionmoiseev.c10n.C10N
import com.github.rodionmoiseev.c10n.C10NConfigBase
import kotlin.reflect.KClass

/** A factory for producing localized message containers. */
interface MessageFactory {
    /** Returns a message container of the specified type. */
    fun <T : Any> get(type: KClass<T>): T
}

/**
 * A message factory that retrieves localized messages from a resource bundle.
 *
 * @param baseName The fully-qualified base name of the resource bundle.
 */
class ResourceBundleMessageFactory(baseName: String) : MessageFactory {
    private val factory = C10N.createMsgFactory(object : C10NConfigBase() {
        override fun configure() {
            bindBundle(baseName)
        }
    })

    override fun <T : Any> get(type: KClass<T>): T = factory.get(type.java)
}
