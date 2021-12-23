package github.cli.app.tweet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public record Tweet(String id, String message, String author) {

    @Override
    public String toString() {
        return "Tweet by %s with id of '%s'".formatted(author, id);
    }

    public static class Deserializer extends StdDeserializer<Tweet> {

        public Deserializer(Class<?> vc) {
            super(vc);
        }

        public Deserializer() {
            this(null);
        }

        @Override
        public Tweet deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode res = p.getCodec().readTree(p);
            JsonNode data = res.get("data");

            String author = res.get("includes").get("users").get(0).get("username").asText();
            String message = data.get("text").asText();
            String id = data.get("id").asText();

            return new Tweet(id, message, author);
        }
    }

}
