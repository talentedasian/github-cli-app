package github.cli.app.user;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public record User(String id) {

    public static class Deserializer extends StdDeserializer<User> {

        public Deserializer(Class<?> vc) {
            super(vc);
        }

        public Deserializer() {
            this(null);
        }

        @Override
        public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode res = p.getCodec().readTree(p);
            return new User(res.get("data").get("id").asText());
        }

    }

}
