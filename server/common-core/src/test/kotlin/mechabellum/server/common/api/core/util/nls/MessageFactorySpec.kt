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

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

interface TestMessages {
    val propertyKey: String

    fun functionKey(): String

    companion object :
        TestMessages by ResourceBundleMessageFactory(TestMessages::class.qualifiedName!!).get(TestMessages::class)
}

object ResourceBundleMessageFactorySpec : SubjectSpek<TestMessages>({
    subject { TestMessages }

    describe("message factory") {
        it("should return message for property key") {
            subject.propertyKey shouldEqual "property key message"
        }

        it("should return message for function key") {
            subject.functionKey() shouldEqual "function key message"
        }
    }
})
