package hitrash.jumismo.android.uoc.edu.hitrash.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {

    private  String id;
    private String comment;
    private Date publicationDate;
    private User author;
    private Group group;

    public Comment() {
        super();
    }

    public Comment(String id, String comment, Date publicationDate, User author, Group group) {
        this.id = id;
        this.comment = comment;
        this.publicationDate = publicationDate;
        this.author = author;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void parseFromJSON(JSONObject jsonObject) {

        try {
            this.id = jsonObject.getString("_id");
            this.comment = jsonObject.getString("comment");
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            this.publicationDate = formatter.parse(jsonObject.getString("publicationDate"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
