package aiss.peertubeminer.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {

    @JsonProperty("id")
    private String id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("createdOn")
    @JsonAlias("createdAt") // PeerTube manda createdAt
    private String createdOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }
}
