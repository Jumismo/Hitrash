package hitrash.jumismo.android.uoc.edu.hitrash.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Group {

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

    public Group(String name, String description, String location, Date date, Boolean isActive, Boolean isCleaningGroup, HikingTrail hikingTrail, List<User> users) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.isActive = isActive;
        this.isCleaningGroup = isCleaningGroup;
        this.hikingTrail = hikingTrail;
        this.users = users;
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
}
