package com.onaio.steps.modelViewWrapper;


import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberViewWrapperTest {

    private NewMemberActivity newMemberActivity;
    private MemberViewWrapper memberViewWrapper;
    private String date;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    public String error_string= "Invalid %s, please fill or correct the following fields: %s";
    @Before
    public void Setup(){
        newMemberActivity = Robolectric.setupActivity(NewMemberActivity.class);
        memberViewWrapper = new MemberViewWrapper(newMemberActivity);
        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());

    }

    @Test
    public void ShouldRaiseExceptionWhenSurnameIsInvalid() throws InvalidDataException {
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"70");
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        surname.setText("");
        firstName.setText("Manisha");
        gender.check(R.id.female_selection);
        age.setText("23");
        Household household = new Household("1","Any Household", "123456789","", InterviewStatus.NOT_SELECTED, date,"Dummy comments");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string,"Member",getStringValue(R.string.member_family_surname_hint)));

        memberViewWrapper.getFromView(household);
    }

    @Test
    public void ShouldRaiseExceptionWhenFirstNameIsInvalid() throws InvalidDataException {
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"70");
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        surname.setText("Rana");
        firstName.setText("");
        gender.check(R.id.female_selection);
        age.setText("23");
        Household household = new Household("1","Any Household", "123456789","", InterviewStatus.NOT_SELECTED, date,"Dummy comments");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string,"Member",getStringValue(R.string.member_first_name_hint)));

        memberViewWrapper.getFromView(household);
    }

    @Test
    public void ShouldRaiseExceptionWhenGenderIsInvalid() throws InvalidDataException {
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"70");
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        surname.setText("Rana");
        firstName.setText("Manisha");
        age.setText("23");
        Household household = new Household("1","Any Household", "123456789","", InterviewStatus.NOT_SELECTED,date,"Dummy comments" );

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string,"Member",getStringValue(R.string.member_gender_hint)));

        memberViewWrapper.getFromView(household);
    }


    @Test
    public void ShouldRaiseExceptionWhenAgeIsInvalid() throws InvalidDataException {
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"70");
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        surname.setText("Rana");
        firstName.setText("Manisha");
        gender.check(R.id.female_selection);
        Household household = new Household("1","Any Household", "123456789","", InterviewStatus.NOT_SELECTED,date,"Dummy comments" );

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string,"Member",getStringValue(R.string.age_hint)));

        memberViewWrapper.getFromView(household);
    }

    @Test
    public void ShouldGiveMemberWhenProperDataIsPassed() throws InvalidDataException {
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"70");
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstname = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        surname.setText("Bansal");
        firstname.setText("Rohit");
        gender.check(R.id.male_selection);
        age.setText("15");
        Household household = new Household("1","Any Household", "123456789","", InterviewStatus.NOT_SELECTED,date,"Dummy comments" );

        Member member = memberViewWrapper.getFromView(household);

        Assert.assertTrue(member.getFamilySurname().equals("Bansal"));
        Assert.assertTrue(member.getFirstName().equals("Rohit"));
        Assert.assertTrue(member.getGender().toString().equals("Male"));
        Assert.assertTrue(member.getAge()==15);
    }

    @Test
    public void ShouldRaiseExceptionWhenMultipleFieldsAreEmpty() throws InvalidDataException {
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        Household household = new Household("1","Any Household", "123456789","", InterviewStatus.NOT_SELECTED,date,"Dummy comments" );

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string,"Member",getStringValue(R.string.member_family_surname_hint),getStringValue(R.string.member_first_name_hint),getStringValue(R.string.member_gender_hint),getStringValue(R.string.age_hint)));

        memberViewWrapper.getFromView(household);
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(newMemberActivity);
        keyValueStore.putString(key, value);
    }

    private String getStringValue(int value){

        return newMemberActivity.getString(value);
    }




}