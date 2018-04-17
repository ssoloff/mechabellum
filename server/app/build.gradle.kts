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

plugins {
    application
}

val ktorVersion by extra { "0.9.1" }

repositories {
    maven("https://dl.bintray.com/kotlin/ktor")
}

dependencies {
    compile("ch.qos.logback", "logback-classic", "1.2.1")
    compile("io.ktor", "ktor-server-netty", ktorVersion)
    testCompile("io.ktor", "ktor-server-test-host", ktorVersion)
}

application {
    mainClassName = "mechabellum.server.app.MainKt"
}

tasks {
    "run"(JavaExec::class) {
        val argsAsString = project.findProperty("mechabellum.args")?.toString().orEmpty()
        args = argsAsString.split("""\s+""".toRegex())
    }
}
