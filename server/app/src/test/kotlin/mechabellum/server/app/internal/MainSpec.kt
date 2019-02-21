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

package mechabellum.server.app.internal

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object MainSpec : Spek({
    describe("/") {
        it("should return OK and expected response") {
            withTestApplication(Application::module) {
                with(handleRequest(HttpMethod.Get, "/")) {
                    response.status() shouldEqual HttpStatusCode.OK
                    response.content shouldEqual "Hello World!"
                }
            }
        }
    }

    describe("/demo/") {
        it("should return OK and expected response") {
            withTestApplication(Application::module) {
                with(handleRequest(HttpMethod.Get, "/demo/")) {
                    response.status() shouldEqual HttpStatusCode.OK
                    response.content shouldEqual "HELLO WORLD!"
                }
            }
        }
    }

    describe("/index.html") {
        it("should not handle request") {
            withTestApplication(Application::module) {
                with(handleRequest(HttpMethod.Get, "/index.html")) {
                    requestHandled.shouldBeFalse()
                }
            }
        }
    }
})
