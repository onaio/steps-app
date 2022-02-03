/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.helper;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.model.Gender;

import java.util.ArrayList;
import java.util.List;

public class DataValidator {

    private AppCompatActivity activity;
    private List<String> errorFields;

    public DataValidator(AppCompatActivity activity) {

        this.activity = activity;
        errorFields = new ArrayList<String>();
    }

    public DataValidator validateAgeRange(String ageString, String errorKey, String minString,
                                          String maxString) {
        if (ageString == null || ageString.equals(""))
            return this;
        int minAge = getValue(minString);
        int maxAge = getValue(maxString);
        int age = Integer.parseInt(ageString);
        if (age < minAge || age > maxAge)
            errorFields.add(String.format(errorKey, minAge, maxAge));
        return this;
    }

    public DataValidator validate(Gender gender, String errorKey) {
        if (gender.equals(Gender.NotDefined))
            errorFields.add(errorKey);
        return this;
    }

    public DataValidator validate(String fieldValue, String errorKey) {
        if (fieldValue == null || fieldValue.equals(""))
            errorFields.add(errorKey);
        return this;
    }

    public List<String> finish() {
        return errorFields;
    }

    private int getValue(String key) {
        return Integer.parseInt(KeyValueStoreFactory.instance(activity).getString(key));
    }

}
