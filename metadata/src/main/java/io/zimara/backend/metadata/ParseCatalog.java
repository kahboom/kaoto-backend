package io.zimara.backend.metadata;

import io.zimara.backend.model.Step;

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

public interface ParseCatalog {
    List<Step> parse();
}
