package github;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

public class Credentials {
    private static final  Logger logger = Logger.getLogger(Credentials.class.getName());;

    public static Path defaultConfigPath = Paths.get("creds.json").toAbsolutePath().normalize();

    private static String DEFAULT_CONFIG_NOT_FOUND = """
                    File that contains the credentials is not found on the path '%s', consider using the overloaded
                    method 'load(pathToFile)' if file can be found elsewhere.
                    """.formatted(defaultConfigPath.toString());


    @SuppressWarnings("Singleton")
    private static Credentials creds = null;
    private static Object lock = new Object();

    private final String clientKey;
    private final String secretKey;

    private Credentials(@JsonProperty("clientKey") String clientKey,
                        @JsonProperty("secretKey") String secretKey) {
        this.clientKey = clientKey;
        this.secretKey = secretKey;
    }

    /**
     * Initializes the credentials used to access the GMAIL API using the current directory as the path to the
     * config file.
     */
    public static void load() {
        if (!defaultConfigPath.toFile().exists()) {
            logger.info(DEFAULT_CONFIG_NOT_FOUND);
            return;
        }

        try {
            creds = new ObjectMapper().readValue(defaultConfigPath.toFile(), Credentials.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the config used to access the GMAIL API.
     *
     * @param pathToFile the path to the config file
     */
    public static void load(Path pathToFile) throws IOException {
        creds = new ObjectMapper().readValue(pathToFile.toFile(), Credentials.class);
    }

    /**
     * Initializes the singleton credentials into null.
     */
    public static void unLoad() {
        creds = null;
    }

    /**
     * Obtains a singleton instance of the Credentials.
     *
     * @return the credentials that has been loaded, if it was.
     */
    public static Credentials instance() {
        synchronized (lock) {
            return Objects.requireNonNull(creds, "Credentials has not been initialized yet");
        }
    }

    public String clientKey() {
        return clientKey;
    }

    public String secretKey() {
        return secretKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        if (!Objects.equals(clientKey, that.clientKey)) return false;
        return Objects.equals(secretKey, that.secretKey);
    }

    @Override
    public int hashCode() {
        int result = clientKey != null ? clientKey.hashCode() : 0;
        result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
        return result;
    }
}
