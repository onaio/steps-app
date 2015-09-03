package com.onaio.steps.model.ODKForm.strategy.interfaces;


import android.app.Activity;

import java.io.IOException;

public interface IFormStrategy {
    void saveDataFile(Activity activity, String path) throws IOException;
}
