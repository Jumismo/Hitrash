package hitrash.jumismo.android.uoc.edu.hitrash;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import hitrash.jumismo.android.uoc.edu.hitrash.Model.Group;

public class GroupTest {

    private JSONObject groupJSON;
    private JSONObject hikingTrailJSON;
    Date currentDate = new Date();

    @Before
    public void setUp() throws JSONException {
        hikingTrailJSON = new JSONObject();
        hikingTrailJSON.put("_id", "1234");
        hikingTrailJSON.put("name", "hikingTrailTest");
        hikingTrailJSON.put("province", "provinceTest");
        hikingTrailJSON.put("location", "locationTest");
        hikingTrailJSON.put("distance", "23");
        hikingTrailJSON.put("claims", "67");
        hikingTrailJSON.put("guide", "true");
        hikingTrailJSON.put("hardness", "false");
        hikingTrailJSON.put("isActive", "true");
        hikingTrailJSON.put("informationOffice", "false");
        hikingTrailJSON.put("signalize", "true");

        groupJSON = new JSONObject();
        String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(currentDate);
        groupJSON.put("_id", "1234");
        groupJSON.put("name", "groupTest");
        groupJSON.put("description", "descriptionTest");
        groupJSON.put("location", "locationTest");
        groupJSON.put("date", date);
        groupJSON.put("isActive", "true");
        groupJSON.put("isCleaningGroup", "true");
        groupJSON.put("hikingTrail", hikingTrailJSON);

    }

    @Test
    public void parseGroupTest(){
        Group group = new Group();
        group.parseFromJSON(groupJSON);

        assertTrue(group.getId().equals("1234"));
        assertTrue(group.getName().equals("groupTest"));
        assertTrue(group.getDescription().equals("descriptionTest"));
        assertTrue(group.getLocation().equals("locationTest"));
        assertTrue(group.getDate().equals(currentDate));
        assertTrue(group.getHikingTrail().getName().equals("hikingTrailTest"));
    }
}
