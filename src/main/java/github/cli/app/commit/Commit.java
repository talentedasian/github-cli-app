package github.cli.app.commit;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public record Commit(String id, String message) {

    public static class Deserializer extends StdDeserializer<Commit> {

        public Deserializer(Class<?> vc) {
            super(vc);
        }

        public Deserializer() {
            this(null);
        }

        @Override
        public Commit deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode sha = p.getCodec().readTree(p);
            return new Commit(sha.get("sha").asText(), sha.get("commit").get("message").asText());
        }
    }

}
