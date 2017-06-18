package uff.ic.swlab;

public class SWLabServer {

    private static final String PRIMARY_HOSTNAME = "swlab.ic.uff.br";
    private static final String PRIMARY_PORT = "";

    private static final String ALTERNATE_HOSTNAME = "www.paes-leme.name";
    private static final String ALTERNATE_PORT = ":8080";

    public static final String HOSTNAME = ALTERNATE_HOSTNAME;
    public static final String BASE_URL = "http://" + ALTERNATE_HOSTNAME + ALTERNATE_PORT + "/";

}
