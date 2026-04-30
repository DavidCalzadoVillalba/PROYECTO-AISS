package aiss.peertubeminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias; // AÑADIR IMPORT

public class Channel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @JsonAlias("displayName") // PeerTube a veces manda displayName
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("createdTime")
    @JsonAlias("createdAt") // PeerTube manda createdAt
    private String createdTime;

    @JsonProperty("videos")
    private List<Video> videos;

    public Channel() {
        this.videos = new ArrayList<>();
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", videos=" + videos +
                '}';
    }
}
