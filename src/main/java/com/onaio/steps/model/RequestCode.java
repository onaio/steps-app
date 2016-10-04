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

public enum RequestCode {
    SETTINGS(1),
    NEW_HOUSEHOLD(2),
    NEW_MEMBER(3),
    EDIT_HOUSEHOLD(4),
    EDIT_MEMBER(5),
    IMPORT(6),
    SURVEY(7),
    NEW_PARTICIPANT(8), EDIT_PARTICIPANT(9);

    private final int code;
    RequestCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
