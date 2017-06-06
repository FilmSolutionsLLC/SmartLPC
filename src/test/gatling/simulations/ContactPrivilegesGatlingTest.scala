import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the ContactPrivileges entity.
 */
class ContactPrivilegesGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connection("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the ContactPrivileges entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
        .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all contactPrivileges")
            .get("/api/contact-privileges")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new contactPrivileges")
            .post("/api/contact-privileges")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "name":"SAMPLE_TEXT", "title":"SAMPLE_TEXT", "description":"SAMPLE_TEXT", "author":"SAMPLE_TEXT", "exec":null, "captioning":null, "downloadType":"0", "email":null, "print":null, "lockApproveRestriction":null, "priorityPix":null, "releaseExclude":null, "viewSensitive":null, "watermark":null, "watermarkInnerTransparency":null, "watermarkOuterTransparency":null, "internal":null, "vendor":null, "restartRole":"SAMPLE_TEXT", "restartImage":"SAMPLE_TEXT", "restartPage":null, "lastLoginDt":"2020-01-01T00:00:00.000Z", "lastLogoutDt":"2020-01-01T00:00:00.000Z", "disabled":null, "welcomeMessage":null, "isABCViewer":null, "talentManagement":null, "signoffManagement":null, "datgeditManagement":null, "createdDate":"2020-01-01T00:00:00.000Z", "updatedDate":"2020-01-01T00:00:00.000Z", "expireDate":"2020-01-01T00:00:00.000Z", "restartFilter":"SAMPLE_TEXT", "restartColumns":"0", "restartImagesPerPage":"0", "restartImageSize":"SAMPLE_TEXT", "restartTime":null, "showFinalizations":null, "readOnly":null, "hasVideo":null, "globalAlbum":null, "seesUntagged":null, "loginCount":"0", "exclusives":null, "defaultAlbum":null, "critiqueIt":null, "adhocLink":null, "retouch":null, "fileUpload":null, "deleteAssets":null}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_contactPrivileges_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created contactPrivileges")
                .get("${new_contactPrivileges_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created contactPrivileges")
            .delete("${new_contactPrivileges_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
