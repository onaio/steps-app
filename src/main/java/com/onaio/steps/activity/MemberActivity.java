package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.MemberActivityFactory;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IResultHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.model.Member;

import java.util.List;

import static com.onaio.steps.helper.Constants.*;

public class MemberActivity extends Activity {

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member);
        populateMember();
        setTitle(member.getFirstName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.member_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void populateMember() {
        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra(MEMBER);
        TextView ageView = (TextView) findViewById(R.id.member_age);
        TextView genderView = (TextView) findViewById(R.id.member_gender);
        ageView.setText(String.valueOf(member.getAge()));
        genderView.setText(String.valueOf(member.getGender()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IMenuHandler> menuHandlers = MemberActivityFactory.getMenuHandlers(this, member);
        for(IMenuHandler menuHandler:menuHandlers)
            if(menuHandler.shouldOpen(item.getItemId()))
                menuHandler.open();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IResultHandler> menuHandlers = MemberActivityFactory.getMenuResultHandlers(this, member);
        for(IResultHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<IPrepare> menuItemHandlers = MemberActivityFactory.getMenuPreparer(this, member,menu);
        for(IPrepare handler:menuItemHandlers)
            if(handler.shouldInactivate())
                handler.inactivate();
        super.onPrepareOptionsMenu(menu);
        return true;
    }

}
