package io.zimara.backend.metadata.catalog;

import io.zimara.backend.metadata.MetadataCatalog;
import io.zimara.backend.model.Step;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryCatalog implements MetadataCatalog {

    private Map<String, Step> metadataCatalog = new HashMap<>();

    @Override
    public boolean store(List<Step> steps) {
        steps.forEach(s -> metadataCatalog.put(s.getID(), s));
        return true;
    }

    @Override
    public Step searchStepByID(String ID) {
        return metadataCatalog.get(ID);
    }

    @Override
    public Step searchStepByName(String name) {
        for (Map.Entry<String, Step> entry : metadataCatalog.entrySet()) {
            if (name.equalsIgnoreCase(entry.getValue().getName())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<Step> searchStepsByName(String name) {
        Collection<Step> steps = new ArrayList<>();

        for (Map.Entry<String, Step> entry : metadataCatalog.entrySet()) {
            if (name.equalsIgnoreCase(entry.getValue().getName())) {
                steps.add(entry.getValue());
            }
        }

        return steps;
    }

    @Override
    public Collection<Step> getAll() {
        return Collections.unmodifiableCollection(this.metadataCatalog.values());
    }
}
