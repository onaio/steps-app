package com.onaio.steps.modelViewWrapper;


import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activities.NewMemberActivity;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
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

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberViewWrapperTest {

    private NewMemberActivity newMemberActivity;
    private MemberViewWrapper memberViewWrapper;
    private String date;
    private final int SURNAME_VIEW_ID = R.id.member_family_surname;
    private final int FIRST_NAME_VIEW_ID = R.id.member_first_name;
    private final int GENDER_VIEW_ID = R.id.member_gender;
    private final int AGE_VIEW_ID = R.id.member_age;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    public String error_string;
    private Household household;
    private Member anotherMember;

    @Before
    public void Setup() {
        newMemberActivity = Robolectric.setupActivity(NewMemberActivity.class);
        memberViewWrapper = new MemberViewWrapper(newMemberActivity);
        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        error_string = getStringValue(R.string.invalid) + " %s, " + getStringValue(R.string.fill_correct_message) + " %s";
        household = new Household("1", "Any Household", "123456789", "", InterviewStatus.NOT_SELECTED, date, "Dummy comments");
        anotherMember = new Member("some surname","firstName",Gender.Female, 22, household, false);
        setValue(Constants.MIN_AGE, "18");
        setValue(Constants.MAX_AGE, "70");
    }

    @Test
    public void ShouldRaiseExceptionWhenSurnameIsInvalid() throws InvalidDataException {
        setSurname("");
        setFirstName("Manisha");
        setGender(R.id.female_selection);
        setAge("23");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Member", getStringValue(R.string.member_family_surname_hint)));

        memberViewWrapper.getFromView(household);
    }

    @Test
    public void ShouldRaiseExceptionWhenFirstNameIsInvalid() throws InvalidDataException {
        setSurname("Rana");
        setFirstName("");
        setGender(R.id.female_selection);
        setAge("23");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Member", getStringValue(R.string.member_first_name_hint)));

        memberViewWrapper.getFromView(household);
    }

    @Test
    public void ShouldRaiseExceptionWhenGenderIsInvalid() throws InvalidDataException {
        setSurname("Rana");
        setFirstName("Manisha");
        setAge("23");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Member", getStringValue(R.string.member_gender_hint)));

        memberViewWrapper.getFromView(household);
    }


    @Test
    public void ShouldRaiseExceptionWhenAgeIsNull() throws InvalidDataException {
        setSurname("Rana");
        setFirstName("Manisha");
        setGender(R.id.female_selection);

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Member", getStringValue(R.string.age_hint)));

        memberViewWrapper.getFromView(household);
    }

    @Test
    public void ShouldRaiseExceptionWhenAgeIsInvalid() throws InvalidDataException {
        setSurname("Rana");
        setFirstName("Manisha");
        setGender(R.id.female_selection);
        setAge("12");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Member", getStringValue(R.string.age_not_in_range)));

        memberViewWrapper.getFromView(household);
    }

    @Test
    public void ShouldGetValuesFromViewAndReturnMember() throws InvalidDataException {
        setSurname("Bansal");
        setFirstName("Rohit");
        setGender(R.id.male_selection);
        setAge("20");

        Member member = memberViewWrapper.getFromView(household);

        Assert.assertTrue(member.getFamilySurname().equals("Bansal"));
        Assert.assertTrue(member.getFirstName().equals("Rohit"));
        Assert.assertTrue(member.getGender().toString().equals("Male"));
        Assert.assertTrue(member.getAge() == 20);
    }

    @Test
    public void ShouldGetValuesFromViewAndUpdateMember() throws InvalidDataException {
        setSurname("Bansal");
        setFirstName("Rohit");
        setGender(R.id.male_selection);
        setAge("20");


        Member member = memberViewWrapper.updateFromView(anotherMember);

        Assert.assertTrue(member.getFamilySurname().equals("Bansal"));
        Assert.assertTrue(member.getFirstName().equals("Rohit"));
        Assert.assertTrue(member.getGender().toString().equals("Male"));
        Assert.assertTrue(member.getAge() == 20);
    }

    @Test
    public void ShouldBeAbleToPopulateViewFromIntentData() {
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView) newMemberActivity.findViewById(R.id.member_age);

        memberViewWrapper.updateView(anotherMember);

        assertEquals("some surname", surname.getText().toString());
        assertEquals("firstName", firstName.getText().toString());
        assertEquals(R.id.female_selection, gender.getCheckedRadioButtonId());
        assertEquals("22", age.getText().toString());
    }

    @Test
    public void ShouldRaiseExceptionWhenMultipleFieldsAreEmpty() throws InvalidDataException {
        setSurname("");
        setFirstName("");
        setAge("");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Member", getStringValue(R.string.member_family_surname_hint), getStringValue(R.string.member_first_name_hint), getStringValue(R.string.member_gender_hint), getStringValue(R.string.age_hint)));

        memberViewWrapper.getFromView(household);
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(newMemberActivity);
        keyValueStore.putString(key, value);
    }

    private String getStringValue(int value) {
        return newMemberActivity.getString(value);
    }

    protected void setSurname(String surname) {
        ((TextView) newMemberActivity.findViewById(SURNAME_VIEW_ID)).setText(surname);
    }

    protected void setFirstName(String firstName) {
        ((TextView) newMemberActivity.findViewById(FIRST_NAME_VIEW_ID)).setText(firstName);
    }

    protected void setGender(int genderId) {
        ((RadioGroup) newMemberActivity.findViewById(GENDER_VIEW_ID)).check(genderId);
    }

    protected void setAge(String age) {
        ((TextView) newMemberActivity.findViewById(AGE_VIEW_ID)).setText(age);
    }
}