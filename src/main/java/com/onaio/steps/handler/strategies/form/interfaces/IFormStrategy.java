package com.onaio.steps.handler.strategies.form.interfaces;


import android.app.Activity;

import com.onaio.steps.helper.DatabaseHelper;

import java.io.IOException;

public interface IFormStrategy {
    public void saveDataFile(Activity activity, String path) throws IOException;
}
