package aiss.videominer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Juan C. Alonso
 */
@Entity
@Table(name = "Caption")
public class Caption {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("link")
    private String link;

    @JsonProperty("language")
    private String language;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Caption{" +
                "id='" + id + '\'' +
                ", link='" + link + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
