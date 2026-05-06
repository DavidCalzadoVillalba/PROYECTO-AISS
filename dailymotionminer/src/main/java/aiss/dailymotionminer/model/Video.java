package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Video {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @JsonAlias("title") // Dailymotion lo llama title
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("releaseTime")
    @JsonAlias("created_time") // Dailymotion lo llama created_time
    private String releaseTime;

    @JsonProperty("user")
    @JsonAlias("owner") // Dailymotion lo llama owner
    private User user; // Renombrado de 'author' a 'user' para cumplir con el UML

    @JsonProperty("comments")
    private List<Comment> comments;

    @JsonProperty("captions")
    private List<Caption> captions;

    // --- NUEVO: Añadimos la propiedad tags ---
    @JsonProperty("tags")
    private List<String> tags;

    // --- GETTERS Y SETTERS ---

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    // --- GETTER Y SETTER PARA LOS TAGS ---
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", user=" + user +
                ", comments=" + comments +
                ", captions=" + captions +
                ", tags=" + tags +
                '}';
    }
}