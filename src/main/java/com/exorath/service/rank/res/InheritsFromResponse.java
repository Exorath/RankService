package com.exorath.service.rank.res;

/**
 * Created by toonsev on 5/18/2017.
 */
public class InheritsFromResponse {
    private boolean inherits;

    public InheritsFromResponse() {}

    public InheritsFromResponse(boolean inherits) {

        this.inherits = inherits;
    }

    public boolean inherits() {
        return inherits;
    }
}
