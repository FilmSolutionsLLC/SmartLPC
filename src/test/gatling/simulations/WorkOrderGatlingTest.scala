import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the WorkOrder entity.
 */
class WorkOrderGatlingTest extends Simulation {

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

    val scn = scenario("Test the WorkOrder entity")
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
            exec(http("Get all workOrders")
            .get("/api/work-orders")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new workOrder")
            .post("/api/work-orders")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "isPrint":null, "isProof":null, "isAbc":null, "isTracking":null, "requestDate":"2020-01-01T00:00:00.000Z", "requestDescription":"SAMPLE_TEXT", "auditedFlag":"0", "poRecord":"SAMPLE_TEXT", "invoiced":"0", "invoiceNumber":"SAMPLE_TEXT", "isAltCredit":null, "overwrite":"SAMPLE_TEXT", "printSet":"SAMPLE_TEXT", "printQuantity":"SAMPLE_TEXT", "printDaysFrom":"SAMPLE_TEXT", "printDaysTo":"SAMPLE_TEXT", "printPagesFrom":"SAMPLE_TEXT", "printPagesTo":"SAMPLE_TEXT", "reminderDate1":"2020-01-01T00:00:00.000Z", "reminderMsg1":"SAMPLE_TEXT", "reminderDate2":"2020-01-01T00:00:00.000Z", "reminderMsg2":"SAMPLE_TEXT", "reminderDate3":"2020-01-01T00:00:00.000Z", "reminderMsg3":"SAMPLE_TEXT", "processingDateRecieved":"2020-01-01T00:00:00.000Z", "processingHDDid":"SAMPLE_TEXT", "processingDateShipped":"2020-01-01T00:00:00.000Z", "processingNote":"SAMPLE_TEXT", "processingLocation":"SAMPLE_TEXT", "processingImageRange":"SAMPLE_TEXT", "processingImageQty":"SAMPLE_TEXT", "dueToClientReminder":"2020-01-01T00:00:00.000Z", "dueToMounterReminder":"2020-01-01T00:00:00.000Z", "recievedFromMounterReminder":"2020-01-01T00:00:00.000Z", "abcInstruction":"SAMPLE_TEXT", "abcRawDvd":"0", "abcTalentCount":"0", "kickBack":"SAMPLE_TEXT", "createdDate":"2020-01-01T00:00:00.000Z", "updatedDate":"2020-01-01T00:00:00.000Z", "completionDate":"2020-01-01T00:00:00.000Z", "durationOfService":"SAMPLE_TEXT", "processingProofShipped":"2020-01-01T00:00:00.000Z"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_workOrder_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created workOrder")
                .get("${new_workOrder_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created workOrder")
            .delete("${new_workOrder_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
