package com.zakharenko;

import java.util.Set;

public class SetWrapper {
    private Set<String> sites;

    public SetWrapper() {}

    public SetWrapper(Set<String> sites) {
        this.sites = sites;
    }

    public Set<String> getsites() {
        return sites;
    }

    public void setUniqueSites(Set<String> sites) {
        this.sites = sites;
    }
}
