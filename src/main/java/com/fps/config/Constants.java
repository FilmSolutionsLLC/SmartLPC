package com.fps.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_NO_SWAGGER = "no-swagger";
    // Spring profile used to disable running liquibase
    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    public static final String SYSTEM_ACCOUNT = "system";


    // Master Database Call
    public static final String MASTER_DATABASE = "master";
    // Slave Database Call
    public static final String SLAVE_DATABASE = "slave";

    public static final String PKO_TAG = "PKO_Tag";
    public static final String SENSITIVE = "Sensitive";
    public static final String MISC_UNKOWN = "Misc_Unknown";
    public static final String BACKGROUND = "Background";
    public static final String CREW = "Crew";
    public static final String ENSEMBLE = "Ensemble";
    public static final String EQUIPMENT = "Equipment";

    public static final String[] HOTKEYS = {"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    public static final String OPEN_WORK_ORDERS = "open_work_orders";
    public static final String OPEN_WORK_ORDERS_GROUPED = "open_work_orders_grouped";
    public static final String PROCESSING_LOG = "open_work_orders_grouped";
    public static final String ALL_WORK_ORDERS = "all_open_work_orders";
    public static final String TO_AUDIT = "to_audit";
    public static final String TO_INVOICE = "to_invoice";
    public static final String INCLUDED_COMP = "included/comp";
    public static final String MY_OPEN_WORK_ORDERS = "my_open_work_orders";


    public static final String CONFIG = "config";
    public static final String SCRATCH_SERVER = "scratch_server";
    public static final String SCRIPT_SERVER = "script_server";
    public static final String SCRIPT_LOCATION = "script_location";

    public static final String LIGHTROOM_EXPORT = "lightroom_export";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LOGOS = "logos";


    public static final String NOTSTARTED = "NOT STARTED";
    public static final String QUEUED = "QUEUED";
    public static final String ERROR = "ERROR";
    public static final String FAILED = "FAILED";
    public static final String WARNING = "WARNING";
    public static final String KILLED = "KILLED";
    public static final String PAUSED = "PAUSED";
    public static final String RESUME = "RESUME";
    public static final String RUNNING = "RUNNING";


    public static final String NEW = "/_NEW/";


    public static final String JC_SERVER = "JCServer";
    public static final String JC_QUEUED = "JCQueued";
    public static final String JC_INPROCESS = "JCInProcess";
    public static final String JC_FAILED = "JCFailed";
    public static final String JC_COMPLETE = "JCComplete";
    public static final String JC_LOGFILE = "JCLogFile";


    public static final String INGEST_SERVER = "IngestSever";
    public static final String INGEST_NEW = "IngestNew";
    public static final String INGEST_QUEUE = "IngestQueued";
    public static final String INGEST_INPROCESS = "IngestInProcess";
    public static final String INGEST_COMPLETED = "IngestComplete";
    public static final String INGEST_ERROR = "IngestError";
    public static final String INGEST_CLEANUP = "IngestCleanup";

    public static final String RUN_INGEST_ONLY = "Run Ingest Only";
    public static final String CONVERT_FROM_MASTERS = "Convert from Masters";
    public static final String CONVERT_FROM_ZOOMS = "Convert from Zooms";
    public static final String CONVERT_FROM_SUPERZOOMS = "Convert from SuperZooms";

    public static final String HIGH = "HIGH";
    public static final String NORMAL = "NORMAL";
    public static final String LOW = "LOW";


    private Constants() {
    }
}
