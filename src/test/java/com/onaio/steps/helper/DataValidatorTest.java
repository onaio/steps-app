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

import static org.junit.Assert.assertEquals;
import static org.robolectric.util.ReflectionHelpers.setStaticField;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.model.Gender;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.List;

public class DataValidatorTest extends StepsTestRunner {
    private AppCompatActivity activity;
    private DataValidator dataValidator;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(ParticipantListActivity.class)
                .create()
                .get();
        dataValidator = new DataValidator(activity);
        setStaticField(KeyValueStoreFactory.class, "keyValueStore", null);
    }

    @Test
    public void validateAgeRangeShouldFailValidation() {

        setValue(Constants.HH_MIN_AGE, "20");
        setValue(Constants.HH_MAX_AGE, "65");

        dataValidator.validateAgeRange("17", activity.getString(R.string.age_not_in_range) + " %s-%s)", Constants.HH_MIN_AGE, Constants.HH_MAX_AGE);
        dataValidator.validateAgeRange("70", activity.getString(R.string.age_not_in_range) + " %s-%s)", Constants.HH_MIN_AGE, Constants.HH_MAX_AGE);
        dataValidator.validateAgeRange("3", activity.getString(R.string.age_not_in_range) + " %s-%s)", Constants.HH_MIN_AGE, Constants.HH_MAX_AGE);

        List<String> errors = dataValidator.finish();

        assertEquals(3, errors.size());
    }

    @Test
    public void validateAgeRangeShouldPassValidation() {

        setValue(Constants.HH_MIN_AGE, "18");
        setValue(Constants.HH_MAX_AGE, "70");

        dataValidator.validateAgeRange("18", activity.getString(R.string.age_not_in_range) + " %s-%s)", Constants.HH_MIN_AGE, Constants.HH_MAX_AGE);
        dataValidator.validateAgeRange("69", activity.getString(R.string.age_not_in_range) + " %s-%s)", Constants.HH_MIN_AGE, Constants.HH_MAX_AGE);
        dataValidator.validateAgeRange("50", activity.getString(R.string.age_not_in_range) + " %s-%s)", Constants.HH_MIN_AGE, Constants.HH_MAX_AGE);

        List<String> errors = dataValidator.finish();

        assertEquals(0, errors.size());
    }

    @Test
    public void validateGenderShouldFail() {
        dataValidator.validate(Gender.NotDefined, activity.getString(R.string.member_gender_hint));
        List<String> errors = dataValidator.finish();

        assertEquals(1, errors.size());
    }

    @Test
    public void validateGenderShouldPass() {
        dataValidator.validate(Gender.Male, activity.getString(R.string.member_gender_hint));
        dataValidator.validate(Gender.Female, activity.getString(R.string.member_gender_hint));
        List<String> errors = dataValidator.finish();

        assertEquals(0, errors.size());
    }

    @Test
    public void validateStringShouldFail() {
        String nullString = null;
        dataValidator.validate("", "String error");
        dataValidator.validate(nullString, "String error");

        List<String> errors = dataValidator.finish();
        assertEquals(2, errors.size());
    }

    @Test
    public void validateStringShouldPass() {
        dataValidator.validate("Precious", "String error");
        dataValidator.validate("Property", "String error");

        List<String> errors = dataValidator.finish();
        assertEquals(0, errors.size());
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(activity);
        keyValueStore.putString(key, value);
    }
}