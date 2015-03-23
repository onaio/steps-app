package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.HouseholdActivityFactory;
import com.onaio.steps.activityHandler.Interface.IHandler;
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

import static com.onaio.steps.model.HouseholdStatus.SELECTED;

public class SelectParticipantHandler implements IHandler, IPrepare {

    private final int MENU_ID = R.id.action_select_participant;
    private final int MAX_RE_ELECT_COUNT = 2;
    private ListActivity activity;
    private Household household;
    private Menu menu;
    private DialogInterface.OnClickListener emptyListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    };

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
        if(ReElectReason.getAll(new DatabaseHelper(activity),household).size() > MAX_RE_ELECT_COUNT)
            Dialog.notify(activity, emptyListener, R.string.participant_no_re_elect_message_because_of_count);
        else
            trySelectingParticipant();
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {}

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }

    @Override
    public boolean shouldInactivate() {
        boolean noMember = Member.numberOfMembers(new DatabaseHelper(activity), household) == 0;
        boolean noSelection = household.getStatus() == HouseholdStatus.OPEN;
        boolean selected = household.getStatus() == HouseholdStatus.SELECTED;
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
            case OPEN: selectParticipant();
                break;
            case SELECTED: Dialog.confirm(activity, confirmListener, emptyListener, confirmation);
                break;
            case DEFERRED: Dialog.confirm(activity, confirmListener, emptyListener, confirmation);
                break;
            default: Dialog.notify(activity, emptyListener, R.string.participant_no_re_elect_message_because_of_status);
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
        List<IPrepare> bottomMenus = HouseholdActivityFactory.getHouseholdBottomMenuItemPreparer(activity, household);
        for(IPrepare menu:bottomMenus)
            if(menu.shouldInactivate())
                menu.inactivate();
            else
                menu.activate();
    }


    private void updateHousehold(Member selectedMember) {
        household.setSelectedMember(String.valueOf(selectedMember.getId()));
        household.setStatus(SELECTED);
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
        int totalMembers = Member.numberOfMembers(new DatabaseHelper(activity.getApplicationContext()), household);
        Random random = new Random();
        int selectedParticipant = random.nextInt(totalMembers);
        return (Member) listView.getItemAtPosition(selectedParticipant);
    }

    public SelectParticipantHandler withMenu(Menu menu){
        this.menu = menu;
        return this;
    }
}
