package com.example.springweb;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the contract-first OpenAPI document is present at the project root
 * and is structurally valid.
 */
class OpenApiContractTest {

    @Test
    @SuppressWarnings("unchecked")
    void openApiContractExistsAndIsParseable() throws Exception {
        Path spec = Path.of("openapi.yaml");
        assertThat(Files.exists(spec)).as("openapi.yaml must exist at project root").isTrue();

        try (InputStream in = Files.newInputStream(spec)) {
            Map<String, Object> root = new Yaml().load(in);

            assertThat(root).containsKey("openapi");
            assertThat(root).containsKey("info");
            assertThat(root).containsKey("paths");

            Map<String, Object> paths = (Map<String, Object>) root.get("paths");
            assertThat(paths)
                    .containsKey("/api/v1/accounts")
                    .containsKey("/api/v1/accounts/{id}");

            Map<String, Object> collection = (Map<String, Object>) paths.get("/api/v1/accounts");
            assertThat(collection).containsKeys("get", "post");

            Map<String, Object> item = (Map<String, Object>) paths.get("/api/v1/accounts/{id}");
            assertThat(item).containsKeys("get", "put", "delete");
        }
    }
}
