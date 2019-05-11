package hitrash.jumismo.android.uoc.edu.hitrash;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import hitrash.jumismo.android.uoc.edu.hitrash.Model.Comment;

public class CommentTest {

    private JSONObject commentJSON;
    private JSONObject userJSON;
    Date currentDate = new Date();

    @Before
    public void setUp() throws JSONException {
        userJSON = new JSONObject();
        userJSON.put("_id", "1234");
        userJSON.put("name", "usuarioTest");
        userJSON.put("password", "passwordTest");
        userJSON.put("email", "emailTest@test.org");
        userJSON.put("isActive", "false");
        userJSON.put("isAdmin", "false");

        commentJSON = new JSONObject();
        commentJSON.put("_id", "1234");
        commentJSON.put("comment", "commentName");
        commentJSON.put("publicationDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(currentDate));
        commentJSON.put("author", userJSON);
    }

    @Test
    public void parseUserTest(){
        Comment comment = new Comment();
        comment.parseFromJSON(commentJSON);

        assertTrue(comment.getId().equals("1234"));
        assertTrue(comment.getComment().equals("commentName"));
        assertTrue(comment.getPublicationDate().equals(currentDate));
        assertTrue(comment.getAuthor().getName().equals("usuarioTest"));
    }
}
