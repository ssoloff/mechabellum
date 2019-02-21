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

val kluentVersion: String by rootProject
val kotlinVersion: String by rootProject
val spekVersion: String by rootProject

dependencies {
    "implementation"(kotlin("reflect", kotlinVersion))
    "implementation"("nl.jqno.equalsverifier", "equalsverifier", "3.1.4")
    "implementation"("org.amshove.kluent", "kluent", kluentVersion)
    "implementation"("org.jetbrains.spek", "spek-api", spekVersion) {
        exclude("org.jetbrains.kotlin")
    }
    "implementation"("org.jetbrains.spek", "spek-subject-extension", spekVersion) {
        exclude("org.jetbrains.kotlin")
    }
}
