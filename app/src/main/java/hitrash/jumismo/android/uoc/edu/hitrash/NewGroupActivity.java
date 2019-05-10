package hitrash.jumismo.android.uoc.edu.hitrash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.Group;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class NewGroupActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener  {

    private TextView dateGroupEditText;
    private TextView groupNameInput;
    private TextView groupDescriptionInput;
    private TextView locationGroupEditText;

    private ListView listViewUsers;

    private ImageButton acceptButtonGroup;

    private List<User> userList;
    private List<String> userListName;
    private boolean isCleaningClaim;
    private String id_hiking_trail;

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        dateGroupEditText = (TextView) findViewById(R.id.dateGroupEditText);
        groupNameInput = (TextView) findViewById(R.id.groupNameInput);
        groupDescriptionInput = (TextView) findViewById(R.id.groupDescriptionInput);
        locationGroupEditText = (TextView) findViewById(R.id.locationGroupEditText);

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        acceptButtonGroup = (ImageButton) findViewById(R.id.acceptButtonGroup);


        dateGroupEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(NewGroupActivity.this);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        final List<User> userList = new ArrayList<User>();
        final List<String> userListName = new ArrayList<String>();

        AsyncHttpUtils.get(Constants.URI_ACTIVE_USERS, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    JSONArray data = response.getJSONArray("data");
                    for(int i = 0; i < data.length(); i++)
                    {
                        JSONObject userJSON = data.getJSONObject(i);
                        User user = new User();
                        user.parseFromJSON(userJSON);

                        userList.add(user);
                        userListName.add(user.getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        listViewUsers = (ListView)findViewById(R.id.listViewUsers);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, userListName);
        listViewUsers.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        isCleaningClaim = intent.getBooleanExtra("isCleaningClaim", false);
        id_hiking_trail = intent.getStringExtra("id_hiking_trail");

        acceptButtonGroup.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                RequestParams rp = new RequestParams();
                rp.add("name", groupNameInput.getText().toString());
                rp.add("description", groupDescriptionInput.getText().toString());
                rp.add("location", locationGroupEditText.getText().toString());

                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateGroupEditText.getText().toString());
                    String dateRP = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
                    rp.add("date", dateRP);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                rp.add("id_hiking_trail", id_hiking_trail);
                rp.add("isCleaningGroup", String.valueOf(isCleaningClaim));
                int len = listViewUsers.getCount();
                SparseBooleanArray checked = listViewUsers.getCheckedItemPositions();
                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {
                        User item = userList.get(i);

                        rp.add("users[]", item.getId());
                        /* do whatever you want with the checked item */
                    }
                }

                AsyncHttpUtils.post(Constants.URI_NEW_GROUP, rp, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            Group group = new Group();
                            group.parseFromJSON(data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        dateGroupEditText.setText(getString(R.string.calendar_date_picker_result_values, String.format("%02d", dayOfMonth), String.format("%02d", monthOfYear), year));

        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(NewGroupActivity.this);
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = (CalendarDatePickerDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialogFragment != null) {
            calendarDatePickerDialogFragment.setOnDateSetListener(this);
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        dateGroupEditText.setText(dateGroupEditText.getText().toString() + " " + getString(R.string.time_picker_result_value, String.format("%02d", hourOfDay), String.format("%02d", minute)));
    }
}
