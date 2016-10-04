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

package com.onaio.steps.exceptions;

import android.app.Activity;

import com.onaio.steps.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class InvalidDataException extends Exception {

    public InvalidDataException(Activity activity, String modelName, List<String> errorFields) {
       super((activity.getString(R.string.action_settings).equalsIgnoreCase(modelName))?
               activity.getString(R.string.invalid_settings)+" "+StringUtils.join(errorFields.toArray(), ','):(activity.getString(R.string.action_member).equalsIgnoreCase(modelName))?
               activity.getString(R.string.invalid_member)+" "+StringUtils.join(errorFields.toArray(),','):"");

        //super(String.format(activity.getString(R.string.invalid)+" %s, " +activity.getString(R.string.fill_correct_message)+" %s.",modelName, StringUtils.join(errorFields.toArray(),',')));
    }


    public InvalidDataException(List<String> errorMessages) {
        super(StringUtils.join(errorMessages.toArray(),','));
    }



}
