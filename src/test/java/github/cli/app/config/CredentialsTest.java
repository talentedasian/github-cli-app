package github.cli.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gmail.cli.app.Credentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("Domain")
public class CredentialsTest {

    Path defaultPath = Credentials.defaultConfigPath;
    ObjectMapper fileReader = new ObjectMapper();

    @AfterEach
    public void teardown() {
        Credentials.unLoad();
    }

    @Test
    public void defaultCredentialLoadShouldInstantiateGmailApiCredentials() throws Exception{
        Credentials.load();
        var credsToAssert = Credentials.instance();
        Credentials credsOnActualFile = fileReader.readValue(defaultPath.toFile(), Credentials.class);

        assertThat(credsToAssert)
                .isEqualTo(credsOnActualFile);
    }

    @Test
    public void shouldThrowIllegalStateIfCredentialsHaveAlreadyBeenLoaded() throws Exception{
        Credentials.load();

        assertThatThrownBy(() -> Credentials.load())
                .isInstanceOf(IllegalStateException.class);
    }

}
