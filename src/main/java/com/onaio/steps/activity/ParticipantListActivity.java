package com.onaio.steps.activity;


import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.ParticipantListActivityFactory;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.adapter.ParticipantAdapter;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Participant;

import java.util.List;

import static com.onaio.steps.helper.Constants.HEADER_GREEN;

public class ParticipantListActivity  extends ListActivity{

        private DatabaseHelper db;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            db = new DatabaseHelper(getApplicationContext());
            prepareScreen();
            super.onCreate(savedInstanceState);
        }

        @Override
        protected void onResume() {
            prepareScreen();
            super.onResume();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.participant_list_actions, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            List<IMenuHandler> activityHandlers = ParticipantListActivityFactory.getMenuHandlers(this);
            for(IMenuHandler handler : activityHandlers){
                if(handler.shouldOpen(item.getItemId()) )
                    return handler.open();
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            List<IActivityResultHandler> activityHandlers = ParticipantListActivityFactory.getResultHandlers(this);
            for(IActivityResultHandler activityHandler: activityHandlers){
                if(activityHandler.canHandleResult(requestCode))
                    activityHandler.handleResult(data,resultCode);
            }
        }


        public void prepareScreen() {
            setLayout();
            populateParticipants();
            bindParticipantItems();
            setWatermark();
        }

        private void setWatermark() {
            final RelativeLayout homePage = (RelativeLayout)findViewById(R.id.main_layout);
            if(homePage == null) return;
            runOnUiThread(new Runnable() {
                public void run() {
                    Window win = getWindow();
                    View contentView = win.findViewById(Window.ID_ANDROID_CONTENT);
                    ImageView imageView = (ImageView) findViewById(R.id.item_image);
                    imageView.getLayoutParams().height = contentView.getHeight() / 2;
                    imageView.getLayoutParams().width = contentView.getWidth() / 2;
                }
            });
        }

        private void setLayout() {
            setContentView(R.layout.main);
           Button participantHeader = (Button) findViewById(R.id.action_add_household);
           participantHeader.setText(R.string.action_add_participant);
           participantHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_new_member,0 ,0,0);
            setTitle(R.string.participant_header);
            setTitleColor(Color.parseColor(HEADER_GREEN));
        }

        private void bindParticipantItems() {
            ListView households = getListView();
            households.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Participant participant = Participant.find_by(db, id);
                    ParticipantListActivityFactory.getParticipantItemHandler(ParticipantListActivity.this, participant).open();
                }
            });
        }

        private void populateParticipants() {
            List<Participant> participants = Participant.getAllParticipants(db);
            getListView().setAdapter(new ParticipantAdapter(this,participants));
        }

       public void handleCustomMenu(View view){
            List<IMenuHandler> handlers = ParticipantListActivityFactory.getCustomMenuHandler(this);
            for(IMenuHandler handler:handlers)
                if(handler.shouldOpen(view.getId()))
                    handler.open();
        }
    }


