package hitrash.jumismo.android.uoc.edu.hitrash;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class NewGroupActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener  {

    private TextView dateGroupEditText;
    private ListView listViewUsers;

    private List<String> userList;

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        dateGroupEditText = (TextView) findViewById(R.id.dateGroupEditText);
        dateGroupEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(NewGroupActivity.this);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        final List<String> userList = new ArrayList<String>();

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

                        userList.add(user.getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        listViewUsers = (ListView)findViewById(R.id.listViewUsers);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, userList);
        listViewUsers.setAdapter(arrayAdapter);
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
