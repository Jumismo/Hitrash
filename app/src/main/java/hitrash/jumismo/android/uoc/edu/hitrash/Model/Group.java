package hitrash.jumismo.android.uoc.edu.hitrash.Model;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class Group implements Serializable {

    private String id;
    private String name;
    private String description;
    private String location;
    private Date date;
    private Boolean isActive;
    private Boolean isCleaningGroup;
    private HikingTrail hikingTrail;
    private List<Comment> comments = new ArrayList<Comment>();
    private List<User> users = new ArrayList<User>();

    public Group() {
    }

    public Group(String id, String name, String description, String location, Date date, Boolean isActive, Boolean isCleaningGroup, HikingTrail hikingTrail, List<User> users) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.isActive = isActive;
        this.isCleaningGroup = isCleaningGroup;
        this.hikingTrail = hikingTrail;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getCleaningGroup() {
        return isCleaningGroup;
    }

    public void setCleaningGroup(Boolean cleaningGroup) {
        isCleaningGroup = cleaningGroup;
    }

    public HikingTrail getHikingTrail() {
        return hikingTrail;
    }

    public void setHikingTrail(HikingTrail hikingTrail) {
        this.hikingTrail = hikingTrail;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void parseFromJSON(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("_id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.location = jsonObject.getString("location");
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            this.date = formatter.parse(jsonObject.getString("date"));
            this.isActive = Boolean.parseBoolean(jsonObject.getString("isActive"));
            this.isCleaningGroup = Boolean.parseBoolean(jsonObject.getString("isCleaningGroup"));
            HikingTrail hikingTrail = new HikingTrail();
            hikingTrail.parseFromJSON(jsonObject.getJSONObject("hikingTrail"));
            this.hikingTrail = hikingTrail;
            JSONArray usersJSON = jsonObject.getJSONArray("users");
            int userSize = usersJSON.length();

            for(int i =0; i < userSize; i++) {
                User user = new User();
                user.parseFromJSON(usersJSON.getJSONObject(i));
                this.users.add(user);
            }

            JSONArray commentsJSON = jsonObject.getJSONArray("comments");
            int commentSize = commentsJSON.length();

            for(int i =0; i < commentSize; i++) {
                Comment comment = new Comment();
                comment.parseFromJSON(commentsJSON.getJSONObject(i));
                this.comments.add(comment);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
