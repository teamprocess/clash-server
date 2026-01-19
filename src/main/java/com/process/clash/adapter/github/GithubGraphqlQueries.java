package com.process.clash.adapter.github;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GithubGraphqlQueries {

    private final ResourceLoader resourceLoader;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String get(String name) {
        return cache.computeIfAbsent(name, this::loadQuery);
    }

    private String loadQuery(String name) {
        String location = "classpath:graphql/github/" + name + ".graphql";
        Resource resource = resourceLoader.getResource(location);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
        )) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load GraphQL query: " + name, ex);
        }
    }
}
