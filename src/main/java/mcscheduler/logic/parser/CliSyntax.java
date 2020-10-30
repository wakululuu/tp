package mcscheduler.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("hp/");
    public static final Prefix PREFIX_PAY = new Prefix("p/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_ROLE = new Prefix("r/");
    public static final Prefix PREFIX_ROLE_REQUIREMENT = new Prefix("r/");
    public static final Prefix PREFIX_SHIFT = new Prefix("s/");
    public static final Prefix PREFIX_SHIFT_NEW = new Prefix("sn/");
    public static final Prefix PREFIX_SHIFT_OLD = new Prefix("so/");
    public static final Prefix PREFIX_SHIFT_DAY = new Prefix("d/");
    public static final Prefix PREFIX_SHIFT_TIME = new Prefix("t/");
    public static final Prefix PREFIX_UNAVAILABILITY = new Prefix("u/");
    public static final Prefix PREFIX_WORKER = new Prefix("w/");
    public static final Prefix PREFIX_WORKER_ROLE = new Prefix("w/");
    public static final Prefix PREFIX_WORKER_OLD = new Prefix("wo/");
    public static final Prefix PREFIX_WORKER_NEW = new Prefix("wn/");

}
