package com.onaio.steps.activity;

import android.app.Activity;
import android.widget.TextView;

import com.onaio.steps.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewMemberActivityTest {

    private Activity newMemberActivity;

    @Before
    public void setup(){
        newMemberActivity = Robolectric.buildActivity(NewMemberActivity.class).create().get();
    }

    @Test
    public void ShouldPopulateViewOnCreate(){
        assertEquals(R.id.member_form, shadowOf(newMemberActivity).getContentView().getId());
        TextView header = (TextView)newMemberActivity.findViewById(R.id.member_form_header);
        Assert.assertNotNull(header);
        Assert.assertTrue(header.getText().equals("Add New Member"));
    }

    @Test
    public void ShouldBeAbleToSaveMemberFromView(){
        newMemberActivity.findViewById(R.id.ic_done).performClick();

        Mockito.verify(newMemberActivity).finish();
    }



}