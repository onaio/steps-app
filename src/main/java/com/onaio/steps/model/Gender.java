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

package com.onaio.steps.model;


import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;

public enum Gender {
    NotDefined(0,R.string.undefined_label),
    Male(1, R.string.male_label),
    Female(2,R.string.female_label);

    private final int intValue;
    private int label;

    Gender(int intValue, int label) {
        this.intValue = intValue;
        this.label = label;
    }

    public int getIntValue(){
        return intValue;
    }
    public String getInternationalizedString(AppCompatActivity activity){

        return activity.getString(label);
    }


}
