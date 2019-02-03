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

import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.LintTask

buildscript {
    val kotlinVersion by extra { "1.2.60" }

    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath("org.jmailen.gradle:kotlinter-gradle:1.19.0")
    }
}

plugins {
    base
    id("com.github.ben-manes.versions") version "0.20.0"
}

val javaVersion by extra { JavaVersion.VERSION_1_8 }
val junitVersion by extra { "5.3.2" }
val kluentVersion by extra { "1.42" }
val kotlinVersion: String by extra
val mockitoVersion by extra { "2.24.0" }
val spekVersion by extra { "1.2.1" }

allprojects {
    group = "mechabellum-server"
    version = "0.1.0"
}

subprojects {
    apply {
        plugin("jacoco")
        plugin("java-library")
        plugin("kotlin")
        plugin("org.jmailen.kotlinter")
    }

    repositories {
        jcenter()
    }

    dependencies {
        "api"(kotlin("stdlib-jdk8", kotlinVersion))
        "api"(kotlin("reflect", kotlinVersion))
        "testImplementation"("org.amshove.kluent", "kluent", kluentVersion)
        "testImplementation"("org.jetbrains.spek", "spek-api", spekVersion) {
            exclude("org.jetbrains.kotlin")
        }
        "testImplementation"("org.jetbrains.spek", "spek-data-driven-extension", spekVersion) {
            exclude("org.jetbrains.kotlin")
        }
        "testImplementation"("org.jetbrains.spek", "spek-subject-extension", spekVersion) {
            exclude("org.jetbrains.kotlin")
        }
        "testImplementation"("org.mockito", "mockito-core", mockitoVersion)
        "testRuntimeOnly"("org.jetbrains.spek", "spek-junit-platform-engine", spekVersion) {
            exclude("org.jetbrains.kotlin")
            exclude("org.junit.platform")
        }
        "testRuntimeOnly"("org.junit.jupiter", "junit-jupiter-engine", junitVersion)
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = javaVersion
    }

    configure<KotlinJvmProjectExtension> {
        experimental.coroutines = Coroutines.ENABLE
    }

    tasks {
        withType<JacocoCoverageVerification> {
            violationRules {
                rule {
                    limit {
                        counter = "INSTRUCTION"
                        minimum = if (project.name == "app") BigDecimal(0.8) else BigDecimal(0.9)
                    }
                }
            }
        }

        withType<JacocoReport> {
            reports {
                html.isEnabled = true
                xml.isEnabled = true
            }
        }

        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = javaVersion.toString()
            }
        }

        withType<Test> {
            useJUnitPlatform {
                includeEngines("spek")
            }
        }

        "check" {
            dependsOn("jacocoTestCoverageVerification")
        }
    }
}

val lintBuildScripts = task<LintTask>("lintBuildScripts") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs lint on the Kotlin build scripts."

    reports = mapOf(
        "checkstyle" to file("$buildDir/reports/ktlint/buildScripts-lint.xml"),
        "plain" to file("$buildDir/reports/ktlint/buildScripts-lint.txt")
    )
    source = fileTree(projectDir) {
        include("**/*.gradle.kts")
    }
}

tasks {
    withType<Wrapper> {
        distributionType = Wrapper.DistributionType.ALL
    }

    "check" {
        dependsOn(lintBuildScripts)
    }
}
