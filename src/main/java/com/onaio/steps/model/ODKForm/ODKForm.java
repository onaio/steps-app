package com.onaio.steps.model.ODKForm;

import android.net.Uri;

public abstract class ODKForm {
    String _id;
    String jrFormId;
    String displayName;
    String jrVersion;

    public ODKForm(String jrVersion, String displayName, String jrFormId, String id) {
        this.jrVersion = jrVersion;
        this.displayName = displayName;
        this.jrFormId = jrFormId;
        _id = id;
    }

    public abstract Uri getUri();

    public abstract String getPath();
}
