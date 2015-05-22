package com.onaio.steps.handler.actions;

import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.onaio.steps.R;
import com.onaio.steps.handler.factories.HouseholdActivityFactory;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.adapters.MemberAdapter;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.SelectedMemberViewWrapper;

import java.util.List;
import java.util.Random;

import static com.onaio.steps.model.InterviewStatus.NOT_DONE;

public class SelectParticipantHandler implements IMenuHandler, IMenuPreparer {

    private final int MENU_ID = R.id.action_select_participant;

    private ListActivity activity;
    private Household household;
    private DatabaseHelper db;
    private android.app.Dialog selection_dialog;

    public SelectParticipantHandler(ListActivity activity, Household household) {
        this(activity, household, new DatabaseHelper(activity), new android.app.Dialog(activity));
    }

    SelectParticipantHandler(ListActivity activity, Household household, DatabaseHelper db, android.app.Dialog androidDialog) {
        this.activity = activity;
        this.household = household;
        this.db = db;
        selection_dialog = androidDialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        popUpMessage();
        return true;
    }

    @Override
    public boolean shouldInactivate() {
        boolean noMember = household.numberOfNonSelectedMembers(db) == 0;
        boolean noSelection = household.getStatus() == InterviewStatus.NOT_SELECTED;
        return noMember || !noSelection;
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View menuItem = activity.findViewById(MENU_ID);
        menuItem.setVisibility(View.VISIBLE);
    }


    private void popUpMessage(){
        selection_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selection_dialog.setContentView(R.layout.select_participant_dialog);
        selection_dialog.setCancelable(true);
        Button confirm = (Button) selection_dialog.findViewById(R.id.confirm);
        Button cancel = (Button) selection_dialog.findViewById(R.id.cancel);

        confirm.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            selection_dialog.dismiss();
            selectParticipant();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                selection_dialog.dismiss();
            }
        });
        selection_dialog.show();
    }


    private void selectParticipant() {
        ListView membersView = activity.getListView();
        Member selectedMember = getSelectedMember(membersView);
        updateHousehold(selectedMember);
        updateView(membersView,selectedMember);
    }

    private void updateView(ListView membersView, Member selectedMember) {
        MemberAdapter membersAdapter = (MemberAdapter) membersView.getAdapter();
        membersAdapter.reinitialize(household.getAllUnselectedMembers(db),String.valueOf(selectedMember.getId()));
        membersAdapter.notifyDataSetChanged();
        populateSelectedMember(selectedMember);
        prepareCustomMenus();
    }

    private void populateSelectedMember(Member selectedMember) {
        new SelectedMemberViewWrapper()
                .populate(household, selectedMember, activity);
    }

    private void prepareCustomMenus() {
        List<IMenuPreparer> bottomMenus = HouseholdActivityFactory.getCustomMenuPreparer(activity, household);
        for(IMenuPreparer menu:bottomMenus)
            if(menu.shouldInactivate())
                menu.inactivate();
            else
                menu.activate();
    }


    private void updateHousehold(Member selectedMember) {
        household.setSelectedMemberId(String.valueOf(selectedMember.getId()));
        household.setStatus(NOT_DONE);
        household.update(new DatabaseHelper(activity));
    }

    private Member getSelectedMember(ListView listView) {
        Member randomMember = getRandomMember(listView);
        while(household.getSelectedMemberId() != null && household.getSelectedMemberId().equals(String.valueOf(randomMember.getId()))){
            randomMember = getRandomMember(listView);
        }
        return randomMember;
    }

    private Member getRandomMember(ListView listView) {
        int totalMembers = household.numberOfNonSelectedMembers(db);
        Random random = new Random();
        int selectedParticipant = random.nextInt(totalMembers);
        return (Member) listView.getItemAtPosition(selectedParticipant);
    }

    protected View getView() {
        LayoutInflater factory = LayoutInflater.from(activity);
        return factory.inflate(R.layout.selection_confirm, null);
    }

}
