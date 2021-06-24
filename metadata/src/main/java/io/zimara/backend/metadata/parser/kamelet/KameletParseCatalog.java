package io.zimara.backend.metadata.parser.kamelet;

import io.quarkus.arc.log.LoggerName;
import io.zimara.backend.metadata.ParseCatalog;
import io.zimara.backend.model.Step;
import io.zimara.backend.model.step.kamelet.KameletStep;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jboss.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KameletParseCatalog implements ParseCatalog {

    @LoggerName("KameletParseCatalog")
    private Logger log;

    private Yaml yaml = new Yaml(new Constructor(KameletStep.class));
    //  private Yaml yaml = new Yaml(new Constructor(Map.class));
    private List<Step> steps = new ArrayList<>();
    private Path repository;

    public KameletParseCatalog(String url, String tag) throws GitAPIException, IOException {
        repository = Files.createTempDirectory("kamelet-catalog");
        repository.toFile().deleteOnExit();

        CloneCommand cloneCommand = new CloneCommand();
        cloneCommand.setURI(url);
        cloneCommand.setDirectory(repository.toFile());
        //cloneCommand.setBranch(tag);
        cloneCommand.call();

        parse();
    }

    @Override
    public List<Step> parse() {
        steps.clear();

//        try (Git git = Git.open(repository.toFile())) {
//            PullCommand pull = git.pull();
//            pull.setStrategy(MergeStrategy.RECURSIVE);
//            pull.setRebase(true);
//            pull.call();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Arrays.stream(repository.toFile().listFiles()).forEach(this::parseKamelet);

        return steps;
    }

    private void parseKamelet(File file) {
        try {
            if (isYAML(file)) {
                KameletStep step = yaml.load(new FileReader(file));
                steps.add(step);
                log.debug("Kamelets: " + steps.size());
            } else {
                log.debug("Skipping " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isYAML(File file) {
        return (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) && !file.getName().startsWith(".");
    }

    public List<Step> getSteps() {
        return steps;
    }
}
