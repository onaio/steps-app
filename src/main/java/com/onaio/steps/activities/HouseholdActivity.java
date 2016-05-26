package com.onaio.steps.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.adapters.MemberAdapter;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.handler.factories.HouseholdActivityFactory;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdVisit;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.modelViewWrapper.HouseholdViewWrapper;
import com.onaio.steps.modelViewWrapper.SelectedMemberViewWrapper;

import java.util.List;

import static com.onaio.steps.model.InterviewStatus.DEFERRED;
import static com.onaio.steps.model.InterviewStatus.INCOMPLETE;
import static com.onaio.steps.model.InterviewStatus.NOT_DONE;

public class HouseholdActivity extends ListActivity {

    private Household household;
    private DatabaseHelper db;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.household);

        activity = this;

        household = (Household) getIntent().getSerializableExtra(Constants.HH_HOUSEHOLD);
        db = new DatabaseHelper(this);

        styleActionBar();
        handleMembers();
        prepareCustomMenu();
        populateMessage();
    }

    private void populateMessage() {
        TextView viewById = (TextView) findViewById(R.id.survey_message);
        switch (household.getStatus()) {
            case DONE:
                viewById.setText(R.string.survey_done_message);
                viewById.setTextColor(Color.parseColor(Constants.TEXT_GREEN));
                break;
            case INCOMPLETE_REFUSED:
                viewById.setText(R.string.survey_partially_completed);
                viewById.setTextColor(Color.RED);
                break;
            case REFUSED:
                viewById.setText(R.string.survey_refused_message);
                viewById.setTextColor(Color.RED);
                break;
            default:
                viewById.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareCustomMenu();
        populateMembers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IMenuHandler> menuHandlers = HouseholdActivityFactory.getMenuHandlers(this, household);
        for (IMenuHandler menuHandler : menuHandlers)
            if (menuHandler.shouldOpen(item.getItemId()))
                menuHandler.open();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.household_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IActivityResultHandler> menuHandlers = HouseholdActivityFactory.getResultHandlers(this, household);
        for (IActivityResultHandler menuHandler : menuHandlers)
            if (menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    public void handleCustomMenu(View view) {
        List<IMenuHandler> bottomMenuItem = HouseholdActivityFactory.getCustomMenuHandler(this, household);
        for (IMenuHandler menuItem : bottomMenuItem)
            if (menuItem.shouldOpen(view.getId()))
                menuItem.open();


    }

    /**
     * added in the view action entry
     *
     * @param view
     */
    public void addChild(View view) {

        if (household == null) return;
        Intent intent = new Intent(activity, NewChildActivity.class);
        intent.putExtra(Constants.HH_HOUSEHOLD, household);
        activity.startActivityForResult(intent, RequestCode.NEW_MEMBER.getCode());

    }

    public void addCareGiver(View view) {
        try {
            if (household == null) return;
            Intent intent = new Intent(activity, CareGiversActivity.class);
            intent.putExtra(Constants.HH_HOUSEHOLD, household);
            activity.startActivityForResult(intent, RequestCode.NEW_MEMBER.getCode());
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }


    private void styleActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_back);
        TextView idHeader = (TextView) findViewById(R.id.household_id_header);
        TextView numberHeader = (TextView) findViewById(R.id.household_number_header);
        TextView commentView = (TextView) findViewById(R.id.text_view_comment);

        idHeader.setText(String.format(getString(R.string.household_id_hint) + ": %s", household.getName()));
        idHeader.setTextColor(Color.parseColor(Constants.HEADER_GREEN));

        if (!household.getPhoneNumber().isEmpty()) {
            numberHeader.setText(String.format(getString(R.string.phone_number) + " %s", household.getPhoneNumber()));

        }
        if (!household.getComments().equals("")) {
            commentView.setText(household.getComments());
        }

        actionBar.setTitle("");
    }

    private void handleMembers() {
        populateMembers();
        bindMembers();

        String interviewEligibility = "";//household.getInteviewEligibility();


        RadioGroup group = (RadioGroup) this.findViewById(R.id.rGrp_household_eligibility);
        final LinearLayout otherSpecifyLayout = (LinearLayout) this.findViewById(R.id.layout_other_specify);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                if (radioButton.getText().toString().equalsIgnoreCase(getString(R.string.other))) {
                    otherSpecifyLayout.setVisibility(View.VISIBLE);
                    EditText txtOthersSpecify = (EditText) activity.findViewById(R.id.others_specify);
                    txtOthersSpecify.requestFocus();
                } else {
                    otherSpecifyLayout.setVisibility(View.GONE);

                }
            }
        });

        View householdEligibilitySaveBtn = findViewById(R.id.action_save_household_eligibility);
        final View eligibilityQuestions = findViewById(R.id.household_eligibility_questions);
        final View eligibleMembers = findViewById(R.id.layout_any_eligible_members);
        // final View addMember = findViewById(R.id.action_add_member);

        householdEligibilitySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RadioGroup rg = (RadioGroup) activity.findViewById(R.id.rGrp_household_eligibility);
                String eligibilityRadioBtnValue = ((RadioButton) activity.findViewById(rg.getCheckedRadioButtonId())).getText().toString();

                if (eligibilityRadioBtnValue.equalsIgnoreCase(activity.getString(R.string.consent))) {
                    view.setVisibility(View.GONE);
                    eligibilityQuestions.setVisibility(View.GONE);
                    eligibleMembers.setVisibility(View.VISIBLE);
                    saveVisit();
                } else {
                    saveVisit();
                    activity.finish();
                }


            }
        });

        RadioGroup householdEligibilityRadioGroup = (RadioGroup) this.findViewById(R.id.rGrp_household_eligible_members);
        final Button addChildBtn = (Button) this.findViewById(R.id.action_add_child);


        householdEligibilityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                if (radioButton.getText().toString().equalsIgnoreCase(getString(R.string.yes))) {
                    //show add member button
                    addChildBtn.setVisibility(View.VISIBLE);
                } else {
                    //hide add member button
                    addChildBtn.setVisibility(View.GONE);

                }
            }
        });


        // FIXME to use the household_visits table
        //check if consent has been granted
        HouseholdVisit lastVisit = HouseholdVisit.findByHouseholdId(db, Long.valueOf(household.getId()));

        if (lastVisit.getStatus().equalsIgnoreCase(getString(R.string.consent))) {
            eligibleMembers.setVisibility(View.VISIBLE);

            eligibilityQuestions.setVisibility(View.GONE);
            //hide save button

            householdEligibilitySaveBtn.setVisibility(View.GONE);

            //show add member button
            // addMember.setVisibility(View.VISIBLE);

        }
    }

    private void saveVisit() {

        try {
            HouseholdVisit  householdVisit = new HouseholdViewWrapper(activity).getHouseholdVisit(R.id.rGrp_household_eligibility, R.id.others_specify);
            householdVisit.setHouseholdId(Long.valueOf(household.getId()));
            householdVisit.save(db);

        } catch (InvalidDataException e) {
            Log.e("", e.getMessage());
        }
    }

    private void bindMembers() {
        AdapterView.OnItemClickListener memberItemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Member member = household.findMember(db, id);
                HouseholdActivityFactory.getMemberItemHandler(HouseholdActivity.this, member).open();
            }
        };
        getListView().setOnItemClickListener(memberItemListener);

    }

    private void prepareCustomMenu() {
        List<IMenuPreparer> customMenus = HouseholdActivityFactory.getCustomMenuPreparer(this, household);
        for (IMenuPreparer customMenu : customMenus)
            if (customMenu.shouldInactivate())
                customMenu.inactivate();
            else
                customMenu.activate();
    }

    private void populateMembers() {
        MemberAdapter memberAdapter = new MemberAdapter(this, getMembers(), household.getSelectedMemberId(), household, true);
        getListView().setAdapter(memberAdapter);
        new SelectedMemberViewWrapper().populate(household, household.getSelectedMember(db), this);
    }

    private List<Member> getMembers() {
        InterviewStatus status = household.getStatus();
        if (status.equals(NOT_DONE) || status.equals(DEFERRED) || status.equals(INCOMPLETE))
            return household.getAllUnselectedMembers(db);
        return household.getAllNonDeletedMembers(db);
    }


}
