import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the Projects entity.
 */
class ProjectsGatlingTest extends Simulation {

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

    val scn = scenario("Test the Projects entity")
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
            exec(http("Get all projects")
            .get("/api/projects")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new projects")
            .post("/api/projects")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "name":"SAMPLE_TEXT", "fullName":"SAMPLE_TEXT", "runOfShowFlag":null, "template":null, "labFlag":null, "alfrescoTitle1":"SAMPLE_TEXT", "alfrescoTitle2":"SAMPLE_TEXT", "startDate":"2020-01-01T00:00:00.000Z", "endDate":"2020-01-01T00:00:00.000Z", "actorsWithRights":null, "daysShooting":null, "weeksShooting":null, "notes":"SAMPLE_TEXT", "sensitiveViewing":null, "productionCompanyNotes":"SAMPLE_TEXT", "productionCompanyShippingNumber":"SAMPLE_TEXT", "processingDeliveries":"SAMPLE_TEXT", "processingSpecialInstructions":"SAMPLE_TEXT", "processingWatermark":"SAMPLE_TEXT", "processingCopyright":"SAMPLE_TEXT", "labProofNotes":"SAMPLE_TEXT", "labLastProofImageNumber":"SAMPLE_TEXT", "labLastProofPageNumber":"SAMPLE_TEXT", "labImageNumberSchema":"SAMPLE_TEXT", "labFolderBatchSchema":"SAMPLE_TEXT", "photoLabInfo":"SAMPLE_TEXT", "projectUnitPhotoNotes":"SAMPLE_TEXT", "projectInfoNotes":"SAMPLE_TEXT", "createdDate":"2020-01-01T00:00:00.000Z", "updatedDate":"2020-01-01T00:00:00.000Z", "legacyDirector":"SAMPLE_TEXT", "legacyExecutiveProducer":"SAMPLE_TEXT", "legacyExecutiveProducer2":"SAMPLE_TEXT", "legacyExecutiveProducer3":"SAMPLE_TEXT", "legacyExecutiveProducer4":"SAMPLE_TEXT", "legacyProducer":"SAMPLE_TEXT", "legacyProducer2":"SAMPLE_TEXT", "legacyProducer3":"SAMPLE_TEXT", "legacyProducer4":"SAMPLE_TEXT", "legacyAdditionalTalent":"SAMPLE_TEXT", "themeId":null, "sptPhotoSubtype":"SAMPLE_TEXT", "photoCredit":"SAMPLE_TEXT", "shootDate":"2020-01-01T00:00:00.000Z", "shootDateOverride":null, "unitPhotographerOverride":null, "useSetup":null, "useExif":null, "tagDate":"2020-01-01T00:00:00.000Z", "tagreportIndex":"0", "loginMessage":"SAMPLE_TEXT", "loginMessageActive":null, "topLevelAlbums":null, "enableTertiary":null, "invoiceCreated":null, "price":"SAMPLE_TEXT", "foxTitle":"SAMPLE_TEXT", "isAsset":null, "fullRejection":"0", "disabled":null, "reminderDate":"2020-01-01T00:00:00.000Z", "photoCreditOverride":null}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_projects_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created projects")
                .get("${new_projects_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created projects")
            .delete("${new_projects_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
