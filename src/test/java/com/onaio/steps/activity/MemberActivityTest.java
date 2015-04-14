package com.onaio.steps.activity;

import com.onaio.steps.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberActivityTest {

    private MemberActivity memberActivity;

    @Before
    public void setup(){
        memberActivity = Robolectric.buildActivity(MemberActivity.class).setup().get();
    }

    @Test
    public void ShouldBeAbleToPopulateWithMemberViewAnd(){
        assertEquals(R.id.member, shadowOf(memberActivity).getContentView().getId());
    }





}