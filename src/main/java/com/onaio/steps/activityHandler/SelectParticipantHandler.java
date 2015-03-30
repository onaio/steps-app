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
    private ListActivity activity;
    private Household household;
    private Menu menu;

    public SelectParticipantHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        if(ReElectReason.getAll(new DatabaseHelper(activity),household).size() >= MAX_RE_ELECT_COUNT)
            new Dialog().notify(activity, Dialog.EmptyListener, R.string.participant_no_re_elect_message_because_of_count, R.string.participant_no_re_elect_title);
        else
            trySelectingParticipant();
        return true;
    }

    @Override
    public boolean shouldInactivate() {
        boolean noMember = Member.numberOfNonDeletedMembers(new DatabaseHelper(activity), household) == 0;
        boolean noSelection = household.getStatus() == HouseholdStatus.NOT_SELECTED;
        boolean selected = household.getStatus() == HouseholdStatus.NOT_DONE;
        boolean deferred = household.getStatus() == HouseholdStatus.DEFERRED;
        boolean canSelectParticipant = noSelection || selected || deferred;
        return noMember || !canSelectParticipant;
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

    private void trySelectingParticipant() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View confirmation = factory.inflate(R.layout.selection_confirm, null);
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveReason(confirmation);
                selectParticipant();
            }
        };

        switch(household.getStatus()){
            case NOT_SELECTED: selectParticipant();
                break;
            case NOT_DONE: Dialog.confirm(activity, confirmListener, Dialog.EmptyListener, confirmation, R.string.participant_re_elect_reason_title);
                break;
            case DEFERRED: Dialog.confirm(activity, confirmListener, Dialog.EmptyListener, confirmation, R.string.participant_re_elect_reason_title);
                break;
            default: new Dialog().notify(activity, Dialog.EmptyListener, R.string.participant_no_re_elect_message_because_of_status, R.string.participant_no_re_elect_title);
        }
    }

    private void saveReason(View confirmation) {
        TextView reasonView = (TextView) confirmation.findViewById(R.id.reason);
        ReElectReason reason = new ReElectReason(reasonView.getText().toString(), household);
        reason.save(new DatabaseHelper(activity));
    }

    private void selectParticipant() {
        ListView membersView = activity.getListView();
        updateHousehold(getSelectedMember(membersView));
        updateView(membersView);
    }

    private void updateView(ListView membersView) {
        MemberAdapter membersAdapter = (MemberAdapter) membersView.getAdapter();
        membersAdapter.setSelectedMemberId(household.getSelectedMember());
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
        household.setSelectedMember(String.valueOf(selectedMember.getId()));
        household.setStatus(NOT_DONE);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    private Member getSelectedMember(ListView listView) {
        Member randomMember = getRandomMember(listView);
        while(household.getSelectedMember() != null && household.getSelectedMember().equals(String.valueOf(randomMember.getId()))){
            randomMember = getRandomMember(listView);
        }
        return randomMember;
    }

    private Member getRandomMember(ListView listView) {
        int totalMembers = Member.numberOfNonDeletedMembers(new DatabaseHelper(activity.getApplicationContext()), household);
        Random random = new Random();
        int selectedParticipant = random.nextInt(totalMembers);
        return (Member) listView.getItemAtPosition(selectedParticipant);
    }

    public SelectParticipantHandler withMenu(Menu menu){
        this.menu = menu;
        return this;
    }
}
