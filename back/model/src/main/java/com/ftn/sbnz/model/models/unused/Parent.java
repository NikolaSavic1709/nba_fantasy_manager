package com.ftn.sbnz.model.models.unused;

import org.kie.api.definition.type.Position;

public class Parent {
    @Position(0)
    public String parent;
    @Position(1)
    public String kid;

    public Parent(String parent, String kid) {
        this.parent = parent;
        this.kid = kid;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }
}
