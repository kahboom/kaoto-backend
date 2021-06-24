package io.zimara.backend.metadata.parser.kamelet;

import io.quarkus.arc.log.LoggerName;
import io.zimara.backend.metadata.catalog.InMemoryCatalog;
import io.zimara.backend.model.Step;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class KameletParseCatalogTest {

    @LoggerName("KameletParseCatalogTest")
    Logger log;

    @Test
    void getSteps() throws GitAPIException, IOException {
        KameletParseCatalog kameletParser = new KameletParseCatalog("https://github.com/apache/camel-kamelets.git", "v0.2.1");
        InMemoryCatalog catalog = new InMemoryCatalog();
        catalog.store(kameletParser.getSteps());

        log.debug("Catalog size " + catalog.getAll().size());

        for(Step step : catalog.getAll()) {
            log.debug(step.getID() + ": " + step.getName());
            log.debug(step);
            log.debug("---");
        }
    }
}