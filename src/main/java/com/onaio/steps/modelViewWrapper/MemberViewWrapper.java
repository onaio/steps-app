package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.DataValidator;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberViewWrapper {

    private final DataValidator dataValidator;
    private final int SURNAME_VIEW_ID = R.id.member_family_surname;
    private final int FIRST_NAME_VIEW_ID = R.id.member_first_name;
    private final int GENDER_VIEW_ID = R.id.member_gender;
    private final int AGE_VIEW_ID = R.id.member_age;
    protected List<String> errorFields;
    protected Activity activity;

    public MemberViewWrapper(Activity activity){
        this.activity = activity;
        errorFields = new ArrayList<String>();
        dataValidator = new DataValidator(activity);
    }

    public Member getFromView(Household household) throws InvalidDataException {
        errorFields.clear();
        String surname = getSurname();
        String firstName = getFirstName();
        Gender gender = getGender();
        String age = getAge();
        validateAll(surname, firstName, gender, age);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(activity,getStringValue(R.string.action_member),errorFields);
        return new Member(surname, firstName, gender, Integer.parseInt(age), household, false);
    }

    public Member updateFromView(Member member) throws InvalidDataException {
        errorFields.clear();
        String surname = getSurname();
        String firstName = getFirstName();
        Gender gender = getGender();
        String age = getAge();
        validateAll(surname, firstName, gender, age);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(activity,getStringValue(R.string.action_member),errorFields);
        return new Member(member.getId(), surname, firstName, gender,Integer.parseInt(age),member.getHousehold(),member.getMemberHouseholdId(),member.getDeleted());
    }

    public void updateView(Member member){
        setSurname(member.getFamilySurname());
        setFirstName(member.getFirstName());
        setGender(genderSelection(member.getGender()));
        setAge(String.valueOf(member.getAge()));
    }


    private void validateAll(String surname, String firstName, Gender gender, String age) {
        errorFields = dataValidator.validate(surname, getStringValue(R.string.member_family_surname_hint)).
                validate(firstName, getStringValue(R.string.member_first_name_hint)).
                validate(gender,getStringValue(R.string.member_gender_hint)).
                validate(age, getStringValue(R.string.age_hint)).
                validateAgeRange(age, getStringValue(R.string.age_not_in_range)+" %d-%d)").
                finish();
    }

    protected String getStringValue(int value){
        return activity.getString(value);
    }

    private Gender genderSelection(int genderSelectionId) {
        if(genderSelectionId == R.id.male_selection)
            return Gender.Male;
        if(genderSelectionId == R.id.female_selection)
            return Gender.Female;
        return Gender.NotDefined;
    }

    protected int genderSelection(Gender gender) {
        if (gender.equals(Gender.Male))
            return R.id.male_selection;
        return R.id.female_selection;

    }

    protected String getSurname(){
        return ((TextView) activity.findViewById(SURNAME_VIEW_ID)).getText().toString();
    }

    protected String getFirstName(){
        return ((TextView) activity.findViewById(FIRST_NAME_VIEW_ID)).getText().toString();
    }

    protected Gender getGender(){
        return genderSelection(((RadioGroup) activity.findViewById(GENDER_VIEW_ID)).getCheckedRadioButtonId());
    }

    protected String getAge(){
        return ((TextView) activity.findViewById(AGE_VIEW_ID)).getText().toString();
    }

    protected void setSurname(String surname){
        ((TextView) activity.findViewById(SURNAME_VIEW_ID)).setText(surname);
    }

    protected void setFirstName(String firstName){
        ((TextView) activity.findViewById(FIRST_NAME_VIEW_ID)).setText(firstName);
    }

    protected void setGender(int genderId){
        ((RadioGroup) activity.findViewById(GENDER_VIEW_ID)).check(genderId);
    }

    protected void setAge(String age){
        ((TextView) activity.findViewById(AGE_VIEW_ID)).setText(age);
    }



}
