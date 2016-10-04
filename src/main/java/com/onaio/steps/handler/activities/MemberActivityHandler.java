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

package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.activities.MemberActivity;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Member;

public class MemberActivityHandler implements IListItemHandler {

    private Member member;
    private Activity activity;

    public MemberActivityHandler(Activity activity, Member member) {
        this.activity = activity;
        this.member = member;
    }

    @Override
    public boolean open() {
        if (member == null) return true;
        Intent intent = new Intent(activity, MemberActivity.class);
        intent.putExtra(Constants.HH_MEMBER, member);
        activity.startActivity(intent);
        return true;
    }
}
