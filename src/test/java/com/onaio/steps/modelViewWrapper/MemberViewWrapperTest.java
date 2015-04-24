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
import com.onaio.steps.model.HouseholdStatus;
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
        Household household = new Household("1","Any Household", "123456789","", HouseholdStatus.NOT_SELECTED, date);

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(Constants.ERROR_STRING,"Member",Constants.FAMILY_SURNAME));

        memberViewWrapper.getMember(surname.getId(),firstName.getId(),gender.getId(), age.getId(),household);
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
        Household household = new Household("1","Any Household", "123456789","", HouseholdStatus.NOT_SELECTED, date);

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(Constants.ERROR_STRING,"Member",Constants.FIRST_NAME));

        memberViewWrapper.getMember(surname.getId(),firstName.getId(),gender.getId(), age.getId(),household);
    }

    @Test
    public void ShouldRaiseExceptionWhenGenderIsInvalid() throws InvalidDataException {
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"70");
        String errorString = Constants.ERROR_STRING;
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        surname.setText("Rana");
        firstName.setText("Manisha");
        age.setText("23");
        Household household = new Household("1","Any Household", "123456789","", HouseholdStatus.NOT_SELECTED,date );

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(errorString,"Member",Constants.GENDER));

        memberViewWrapper.getMember(surname.getId(),firstName.getId(),gender.getId(), age.getId(),household);
    }


    @Test
    public void ShouldRaiseExceptionWhenAgeIsInvalid() throws InvalidDataException {
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"70");
        String errorString = Constants.ERROR_STRING;
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        surname.setText("Rana");
        firstName.setText("Manisha");
        gender.check(R.id.female_selection);
        Household household = new Household("1","Any Household", "123456789","", HouseholdStatus.NOT_SELECTED,date );

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(errorString,"Member",Constants.AGE));

        memberViewWrapper.getMember(surname.getId(),firstName.getId(),gender.getId(), age.getId(),household);
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
        Household household = new Household("1","Any Household", "123456789","", HouseholdStatus.NOT_SELECTED,date );

        Member member = memberViewWrapper.getMember(surname.getId(), firstname.getId(), gender.getId(), age.getId(), household);

        Assert.assertTrue(member.getFamilySurname().equals("Bansal"));
        Assert.assertTrue(member.getFirstName().equals("Rohit"));
        Assert.assertTrue(member.getGender().toString().equals("Male"));
        Assert.assertTrue(member.getAge()==15);
    }

    @Test
    public void ShouldRaiseExceptionWhenMultipleFieldsAreEmpty() throws InvalidDataException {
        String errorString = Constants.ERROR_STRING;
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        Household household = new Household("1","Any Household", "123456789","", HouseholdStatus.NOT_SELECTED,date );

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(errorString,"Member",Constants.FAMILY_SURNAME,Constants.FIRST_NAME,Constants.GENDER,Constants.AGE));

        memberViewWrapper.getMember(surname.getId(), firstName.getId(), gender.getId(), age.getId(), household);
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(newMemberActivity);
        keyValueStore.putString(key, value);
    }



}