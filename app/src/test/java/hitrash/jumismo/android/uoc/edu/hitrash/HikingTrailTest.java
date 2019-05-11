package hitrash.jumismo.android.uoc.edu.hitrash;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;

public class HikingTrailTest {

    private JSONObject hikingTrailJSON;

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
    }

    @Test
    public void parseGroupTest(){
        HikingTrail hikingTrail = new HikingTrail();
        hikingTrail.parseFromJSON(hikingTrailJSON);

        assertTrue(hikingTrail.getId().equals("1234"));
        assertTrue(hikingTrail.getName().equals("hikingTrailTest"));
        assertTrue(hikingTrail.getProvince().equals("provinceTest"));
        assertTrue(hikingTrail.getLocation().equals("locationTest"));
        assertTrue(hikingTrail.getDistance().equals(23));
        assertTrue(hikingTrail.getGuide().equals(true));
        assertFalse(hikingTrail.getInformationOffice().equals(true));
    }
}
