package com.ftn.sbnz.model.models.unused;

import org.kie.api.definition.type.Position;

public abstract class Person {
    @Position(0)
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
