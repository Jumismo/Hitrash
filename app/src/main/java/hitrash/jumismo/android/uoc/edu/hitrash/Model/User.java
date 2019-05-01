package hitrash.jumismo.android.uoc.edu.hitrash.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class User {

    private String id;
    private String name;
    private String password;
    private String email;
    private Boolean isAdmin;

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

    public void parseFromJSON(JSONObject data)
    {
        try {
            this.id = data.getString("_id");
            this.name = data.getString("name");
            this.password = data.getString("password");
            this.email = data.getString("email");
            this.isAdmin = Boolean.parseBoolean(data.getString("isAdmin"));
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
