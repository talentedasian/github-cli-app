package github.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.Credentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("Domain")
public class CredentialsTest {

    Path CREDS_PATH = Paths.get("../../creds.json");
    ObjectMapper fileReader = new ObjectMapper();

    @AfterEach
    public void teardown() {
        Credentials.unLoad();
    }

    @Test
    public void shouldBeSameOnFileAfterLoad() throws Exception{
        Credentials.load(CREDS_PATH);
        var credsToAssert = Credentials.instance();
        Credentials credsOnActualFile = fileReader.readValue(CREDS_PATH.toFile(), Credentials.class);

        assertThat(credsToAssert)
                .isEqualTo(credsOnActualFile);
    }

}
