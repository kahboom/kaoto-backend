package io.kaoto.backend.model.deployment.kamelet.step;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.kaoto.backend.KamelPopulator;
import io.kaoto.backend.api.metadata.catalog.StepCatalog;
import io.kaoto.backend.api.service.step.parser.kamelet.KameletStepParserService;
import io.kaoto.backend.model.deployment.kamelet.FlowStep;
import io.kaoto.backend.model.deployment.kamelet.step.choice.Choice;
import io.kaoto.backend.model.deployment.kamelet.step.choice.Otherwise;
import io.kaoto.backend.model.deployment.kamelet.step.choice.SuperChoice;
import io.kaoto.backend.model.step.Branch;
import io.kaoto.backend.model.step.Step;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"choice"})
@JsonDeserialize(using = JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChoiceFlowStep implements FlowStep {

    public static final String CHOICE_LABEL = "choice";

    @JsonProperty(CHOICE_LABEL)
    private SuperChoice choice;

    @JsonCreator
    public ChoiceFlowStep(final @JsonProperty(value = CHOICE_LABEL) SuperChoice choice) {
        super();
        setChoice(choice);
    }

    public ChoiceFlowStep(final Step step, final KamelPopulator kameletPopulator) {
        setChoice(new SuperChoice(step, kameletPopulator));

        final var choice = new SuperChoice();

        List<Choice> choices = new LinkedList<>();

        if (step.getBranches() != null) {
            for (Branch b : step.getBranches()) {
                if (b.getCondition() != null) {
                    choices.add(processChoice(b, kameletPopulator));
                } else {
                    var otherwise = new Otherwise();
                    otherwise.setSteps(kameletPopulator.processSteps(b));
                    choice.setOtherwise(otherwise);
                }
            }
        }
        choice.setChoice(choices);
    }

    private Choice processChoice(final Branch b, final KamelPopulator kameletPopulator) {
        Choice choice = new Choice();

        choice.setSteps(kameletPopulator.processSteps(b));
        if (b.getConditionSyntax() == Branch.ConditionSyntax.JQ) {
            choice.setJq(b.getCondition());
        } else {
            choice.setSimple(b.getCondition());
        }

        return choice;
    }

    @Override
    public Map<String, Object> getRepresenterProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(CHOICE_LABEL, this.getChoice());
        return properties;
    }

    @Override
    public Step getStep(final StepCatalog catalog, final KameletStepParserService kameletStepParserService,
                        final Boolean start, final Boolean end) {
        return this.getChoice().getStep(catalog, CHOICE_LABEL, kameletStepParserService);
    }

    public SuperChoice getChoice() {
        return choice;
    }

    public void setChoice(final SuperChoice choice) {
        this.choice = choice;
    }
}
