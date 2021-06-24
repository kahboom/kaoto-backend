package io.zimara.backend.api;

import io.quarkus.arc.log.LoggerName;
import io.zimara.backend.metadata.MetadataCatalog;
import io.zimara.backend.metadata.ParseCatalog;
import io.zimara.backend.metadata.catalog.InMemoryCatalog;
import io.zimara.backend.metadata.parser.kamelet.KameletParseCatalog;
import io.zimara.backend.model.Step;
import io.zimara.backend.model.View;
import io.zimara.backend.model.step.kamelet.KameletStep;
import io.zimara.backend.model.view.IntegrationView;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 🐱class GetView
 * This endpoint will return a list of views based on the parameters.
 *
 * 🐱example
 *
 * ```
 * YAML
 * ```
 */
@Path("/view")
public class GetViews {

    @LoggerName("GetViews")
    private Logger log;

    private MetadataCatalog catalog;

    public GetViews() {
        catalog = new InMemoryCatalog();
        ParseCatalog parser = null;
        try {
            parser = new KameletParseCatalog("https://github.com/apache/camel-kamelets.git", "v0.2.1");
            catalog.store(parser.parse());
        } catch (GitAPIException e) {
            log.error(e, e);
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    /*
     * 🐱method views:
     * 🐱param yaml: String
     *
     * Based on the YAML provided, offer a list of possible views
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<View> views(@QueryParam("yaml") String yaml) {
        List<View> views = new ArrayList<>();

        List<Step> steps = extractSteps();
        var integrationView = new IntegrationView(steps, "test view");
        views.add(integrationView);

        return views;
    }

    private List<Step> extractSteps() {
        List<Step> steps = new ArrayList<>();

        steps.add(new KameletStep("kamelet", "kamelet", "icon.png", null));
        steps.add(new KameletStep("kamelet2", "kamelet2", "icon.png", null));

        return steps;
    }
}