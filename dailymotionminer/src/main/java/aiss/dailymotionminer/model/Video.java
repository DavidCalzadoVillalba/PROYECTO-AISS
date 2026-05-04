package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Video {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @JsonAlias("title")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("releaseTime")
    @JsonAlias("created_time")
    private String releaseTime;

    @JsonProperty("user")
    @JsonAlias("owner")
    private User author;

    @JsonProperty("comments")
    private List<Comment> comments;

    @JsonProperty("captions")
    private List<Caption> captions;

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

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
    
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Caption> getCaptions() {
        return captions;
    }

    public void setCaptions(List<Caption> captions) {
        this.captions = captions;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", author=" + author +
                ", comments=" + comments +
                ", captions=" + captions +
                '}';
    }
}
