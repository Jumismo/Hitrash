package hitrash.jumismo.android.uoc.edu.hitrash.Model;

import java.util.Date;

public class Comment {

    private String comment;
    private Date publicationDate;
    private User author;
    private Group group;

    public Comment() {
        super();
    }

    public Comment(String comment, Date publicationDate, User author, Group group) {
        this.comment = comment;
        this.publicationDate = publicationDate;
        this.author = author;
        this.group = group;
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
}
