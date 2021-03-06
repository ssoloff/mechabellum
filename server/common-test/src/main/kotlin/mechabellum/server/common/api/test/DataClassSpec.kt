/*
 * Copyright (C) 2019 Mechabellum contributors
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

package mechabellum.server.common.api.test

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotMatch
import org.jetbrains.annotations.NotNull
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import java.lang.reflect.Modifier

/**
 * The purpose of this spec is to ensure full code coverage over data class methods generated by the Kotlin compiler.
 * Unfortunately, JaCoCo has no way to identify these generated methods and thus cannot exclude them from coverage
 * analysis (see https://github.com/jacoco/jacoco/issues/552). This spec is intended as a temporary workaround to
 * prevent coverage from dropping every time we add a data class. It should be removed when the above JaCoCo issue is
 * resolved.
 */
abstract class DataClassSpec(newSubject: () -> Any) : SubjectSpek<Any>({
    subject { newSubject() }

    fun copyByDestructuring(subject: Any): Any {
        val copyMethod = subject.javaClass.methods
            .find { (it.name == "copy") && !Modifier.isStatic(it.modifiers) }
        val componentValues = subject.javaClass.methods
            .filter { it.name.matches("""component\d+""".toRegex()) }
            .sortedBy { it.name.substring("component".length).toInt() }
            .map { it.invoke(subject) }
            .toTypedArray()
        return copyMethod!!.invoke(subject, *componentValues)
    }

    describe("data class") {
        it("should be copyable and destructurable") {
            copyByDestructuring(subject) shouldEqual subject
        }

        it("should be equatable and hashable") {
            EqualsVerifier.forClass(subject.javaClass)
                .withIgnoredAnnotations(NotNull::class.java)
                .verify()
        }

        it("should have a non-default string representation") {
            subject.toString() shouldNotMatch ".+@[0-9a-zA-Z]{1,8}".toRegex()
        }
    }
})
