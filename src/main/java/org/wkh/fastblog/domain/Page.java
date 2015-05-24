package org.wkh.fastblog.domain;

import javax.validation.constraints.NotNull;

public class Page {
    @NotNull
    private final String id;
    @NotNull
    private final String body;

    public Page(String id, String body) {
        this.id = id;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

}
