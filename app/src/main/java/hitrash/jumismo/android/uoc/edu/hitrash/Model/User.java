package hitrash.jumismo.android.uoc.edu.hitrash.Model;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;

public class User {

    private String id;
    private String name;
    private String password;
    private String email;
    private Boolean isAdmin;
    private Boolean isActive;
    private List<HikingTrail> hikingTrails = new ArrayList<HikingTrail>();
    private List<Comment> comments = new ArrayList<Comment>();
    private List<Group> groups = new ArrayList<Group>();

    public User()
    {
        super();
    }

    public User(String id, String name, String password, String email, Boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.isActive = false;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getIsActive() { return isActive; }

    public void setIsActive(Boolean isActive){ this.isActive = isActive;}

    // Método para parsear un objeto recibido en una petición.
    public void parseFromJSON(JSONObject data)
    {
        try {
            this.id = data.getString("_id");
            this.name = data.getString("name");
            this.password = data.getString("password");
            this.email = data.getString("email");
            this.isAdmin = Boolean.parseBoolean(data.getString("isAdmin"));
            this.isActive = Boolean.parseBoolean(data.getString("isActive"));
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
