package hitrash.jumismo.android.uoc.edu.hitrash;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;

public class UserTest {

    private JSONObject userJSON;

    @Before
    public void setUp() throws JSONException {
        userJSON = new JSONObject();
        userJSON.put("_id", "1234");
        userJSON.put("name", "usuarioTest");
        userJSON.put("password", "passwordTest");
        userJSON.put("email", "emailTest@test.org");
        userJSON.put("isActive", "false");
        userJSON.put("isAdmin", "false");
    }

    @Test
    public void parseUserTest(){
        User user = new User();
        user.parseFromJSON(userJSON);

        assertTrue(user.getId().equals("1234"));
        assertTrue(user.getName().equals("usuarioTest"));
        assertTrue(user.getPassword().equals("passwordTest"));
        assertTrue(user.getEmail().equals("emailTest@test.org"));
        assertTrue(user.getIsActive().equals(false));
        assertTrue(user.getAdmin().equals(false));
    }
}
