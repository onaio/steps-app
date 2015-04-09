package com.onaio.steps.activity;


import android.widget.TextView;

import com.onaio.steps.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class EditMemberActivityTest {

    private EditMemberActivity editMemberActivity;

    @Before
    public void setup(){
        editMemberActivity = Robolectric.buildActivity(EditMemberActivity.class).create().get();
    }

    @Test
    public void ShouldPopulateViewWithMemberForm(){

        TextView header = (TextView)editMemberActivity.findViewById(R.id.member_form_header);

        assertEquals(R.id.member_form, shadowOf(editMemberActivity).getContentView().getId());
        assertNotNull(header);
        assertTrue(header.getText().equals("Edit Member"));

    }

}