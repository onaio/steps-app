package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.HouseholdActivityFactory;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;

import java.util.List;
import java.util.Random;

import static com.onaio.steps.model.HouseholdStatus.NOT_DONE;

public class SelectParticipantHandler implements IMenuHandler, IPrepare {

    private final int MENU_ID = R.id.action_select_participant;
    private final int MAX_RE_ELECT_COUNT = 2;
    private final Dialog dialog;
    private ListActivity activity;
    private Household household;
    private Menu menu;
    private DatabaseHelper db;

    public SelectParticipantHandler(ListActivity activity, Household household) {
        this(activity, household, new Dialog(), new DatabaseHelper(activity));
    }

    SelectParticipantHandler(ListActivity activity, Household household, Dialog dialog, DatabaseHelper db) {
        this.activity = activity;
        this.household = household;
        this.dialog = dialog;
        this.db = db;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        confirm();
        return true;
    }

    @Override
    public boolean shouldInactivate() {
        boolean noMember = household.numberOfNonDeletedMembers(db) == 0;
        boolean noSelection = household.getStatus() == HouseholdStatus.NOT_SELECTED;
        boolean selected = household.getStatus() == HouseholdStatus.NOT_DONE;
        boolean deferred = household.getStatus() == HouseholdStatus.DEFERRED;
        boolean maxReElectionReached = ReElectReason.getAll(db, household).size() >= MAX_RE_ELECT_COUNT;
        boolean canSelectParticipant = noSelection || selected || deferred;
        return noMember || !canSelectParticipant || maxReElectionReached;
    }

    @Override
    public void inactivate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(false);
    }

    @Override
    public void activate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(true);
    }

    private void confirm() {
        trySelectingParticipant();
    }

    private void trySelectingParticipant() {
        final View confirmation = getView();
        DialogInterface.OnClickListener confirmListenerForReElection = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveReason(confirmation);
                selectParticipant();
            }
        };
        DialogInterface.OnClickListener confirmListenerForFirstSelection = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectParticipant();
            }
        };

        switch(household.getStatus()){
            case NOT_SELECTED: dialog.confirm(activity, confirmListenerForFirstSelection, Dialog.EmptyListener, R.string.select_participant_message, R.string.select_participant_title);
                break;
            case NOT_DONE: dialog.confirm(activity, confirmListenerForReElection, Dialog.EmptyListener, confirmation, R.string.participant_re_elect_reason_title);
                break;
            case DEFERRED: dialog.confirm(activity, confirmListenerForReElection, Dialog.EmptyListener, confirmation, R.string.participant_re_elect_reason_title);
                break;
            default: new Dialog().notify(activity, Dialog.EmptyListener, R.string.participant_no_re_elect_message_because_of_status, R.string.participant_no_re_elect_title);
        }
    }


    private void saveReason(View confirmation) {
        TextView reasonView = (TextView) confirmation.findViewById(R.id.reason);
        ReElectReason reason = new ReElectReason(reasonView.getText().toString(), household);
        reason.save(db);
    }

    private void selectParticipant() {
        ListView membersView = activity.getListView();
        updateHousehold(getSelectedMember(membersView));
        updateView(membersView);
    }

    private void updateView(ListView membersView) {
        MemberAdapter membersAdapter = (MemberAdapter) membersView.getAdapter();
        membersAdapter.setSelectedMemberId(household.getSelectedMemberId());
        membersAdapter.notifyDataSetChanged();
        prepareBottomMenuItems();
    }

    private void prepareBottomMenuItems() {
        List<IPrepare> bottomMenus = HouseholdActivityFactory.getBottomMenuPreparer(activity, household);
        for(IPrepare menu:bottomMenus)
            if(menu.shouldInactivate())
                menu.inactivate();
            else
                menu.activate();
    }


    private void updateHousehold(Member selectedMember) {
        household.setSelectedMemberId(String.valueOf(selectedMember.getId()));
        household.setStatus(NOT_DONE);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    private Member getSelectedMember(ListView listView) {
        Member randomMember = getRandomMember(listView);
        while(household.getSelectedMemberId() != null && household.getSelectedMemberId().equals(String.valueOf(randomMember.getId()))){
            randomMember = getRandomMember(listView);
        }
        return randomMember;
    }

    private Member getRandomMember(ListView listView) {
        int totalMembers = household.numberOfNonDeletedMembers(db);
        Random random = new Random();
        int selectedParticipant = random.nextInt(totalMembers);
        return (Member) listView.getItemAtPosition(selectedParticipant);
    }

    public SelectParticipantHandler withMenu(Menu menu){
        this.menu = menu;
        return this;
    }

    protected View getView() {
        LayoutInflater factory = LayoutInflater.from(activity);
        return factory.inflate(R.layout.selection_confirm, null);
    }

}
