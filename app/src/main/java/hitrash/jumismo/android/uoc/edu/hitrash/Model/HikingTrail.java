package hitrash.jumismo.android.uoc.edu.hitrash.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HikingTrail {

    private String id;
    private String name;
    private String province;
    private String location;
    private Integer distance;
    private Integer claims;
    private Boolean guide;
    private String hardness;
    private Boolean informationOffice;
    private Boolean signalize;
    private Boolean isActive;
    private User user;
    private List<Group> groups = new ArrayList<Group>();
//    private List<Image> images = new ArrayList<Image>();


    public HikingTrail() {
    }

    public HikingTrail(String id, String name, String province, String location, Integer distance, Integer claims, Boolean guide, String hardness, Boolean informationOffice, Boolean signalize, Boolean isActive, User user) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.location = location;
        this.distance = distance;
        this.claims = claims;
        this.guide = guide;
        this.hardness = hardness;
        this.informationOffice = informationOffice;
        this.signalize = signalize;
        this.isActive = isActive;
        this.user = user;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getClaims() {
        return claims;
    }

    public void setClaims(Integer claims) {
        this.claims = claims;
    }

    public Boolean getGuide() {
        return guide;
    }

    public void setGuide(Boolean guide) {
        this.guide = guide;
    }

    public String getHardness() {
        return hardness;
    }

    public void setHardness(String hardness) {
        this.hardness = hardness;
    }

    public Boolean getInformationOffice() {
        return informationOffice;
    }

    public void setInformationOffice(Boolean informationOffice) {
        this.informationOffice = informationOffice;
    }

    public Boolean getSignalize() {
        return signalize;
    }

    public void setSignalize(Boolean signalize) {
        this.signalize = signalize;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void parseFromJSON(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("_id");
            this.name = jsonObject.getString("name");
            this.province = jsonObject.getString("province");
            this.location = jsonObject.getString("location");
            this.distance = Integer.parseInt(jsonObject.getString("distance"));
            this.claims = Integer.parseInt(jsonObject.getString("claims"));
            this.guide = Boolean.parseBoolean(jsonObject.getString("guide"));
            this.hardness = jsonObject.getString("hardness");
            this.isActive = Boolean.parseBoolean(jsonObject.getString("isActive"));
            this.informationOffice = Boolean.parseBoolean(jsonObject.getString("informationOffice"));
            this.signalize = Boolean.parseBoolean(jsonObject.getString("signalize"));
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
