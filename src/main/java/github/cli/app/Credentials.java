package gmail.cli.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

    private final String apiKey;
    private final String secretKey;
    private final List<String> redirectUris;
    private final String authProvider;
    private final String projectId;
    private final String authUri;
    private final String tokenUri;

    private Credentials(@JsonProperty("apiKey") String apiKey,
                        @JsonProperty("secretKey") String secretKey,
                        @JsonProperty("redirectUris") List<String> redirectUris,
                        @JsonProperty("authProvider") String authProvider,
                        @JsonProperty("projectId") String projectId,
                        @JsonProperty("authUri") String authUri,
                        @JsonProperty("tokenUri") String tokenUri) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.redirectUris = redirectUris;
        this.authProvider = authProvider;
        this.projectId = projectId;
        this.authUri = authUri;
        this.tokenUri = tokenUri;
    }

    /**
     * Initializes the credentials used to access the GMAIL API using the current directory as the path to the
     * config file.
     */
    public static void load() {
        assertCredNotNull();
        try {
            creds = new ObjectMapper().readValue(defaultConfigPath.toFile(), Credentials.class);
        } catch (IOException e) {
            logger.info(DEFAULT_CONFIG_NOT_FOUND);
        }
    }

    /**
     * Initializes the config used to access the GMAIL API.
     *
     * @param pathToFile the path to the config file
     */
    public static void load(Path pathToFile) throws IOException {
        assertCredNotNull();

        creds = new ObjectMapper().readValue(defaultConfigPath.toFile(), Credentials.class);
    }

    /**
     * Initializes the singleton credentials into null.
     *
     * @throws IllegalStateException is thrown if the credentials has not been loaded yet.
     */
    public static void unLoad() {
        if (creds == null)
            throw new IllegalStateException("Cannot unload credentials if it has not been loaded yet.");

        creds = null;
    }

    private static void assertCredNotNull() {
        if (creds != null)
            throw new IllegalStateException("Credentials has already been loaded.");
    }

    /**
     * Obtains a singleton instance of the Credentials.
     *
     * @return the credentials that has been loaded, if it was.
     */
    public static Credentials instance() {
        return Objects.requireNonNull(creds, "Credentials has not been initialized yet");
    }

    public String apiKey() {
        return apiKey;
    }

    public String secretKey() {
        return secretKey;
    }

    public String authUri() {
        return authUri;
    }

    public String authProvider() {
        return authProvider;
    }

    public List<String> redirectUris() {
        return List.copyOf(redirectUris);
    }

    public String projectId() {
        return projectId;
    }

    public String tokenUri() {
        return tokenUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        if (apiKey != null ? !apiKey.equals(that.apiKey) : that.apiKey != null) return false;
        return secretKey != null ? secretKey.equals(that.secretKey) : that.secretKey == null;
    }

    @Override
    public int hashCode() {
        int result = apiKey != null ? apiKey.hashCode() : 0;
        result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
        return result;
    }
}
