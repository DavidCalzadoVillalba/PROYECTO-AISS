package aiss.peertubeminer.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;


public class User {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    @JsonAlias("displayName") // PeerTube suele mandar displayName
    private String name;

    @JsonProperty("user_link")
    @JsonAlias("url") // PeerTube manda la etiqueta url
    private String user_link;

    @JsonProperty("picture_link")
    private String picture_link; 
    // Nota: La foto de perfil en PeerTube viene en un array anidado llamado 'avatars', 
    // es un poco complejo de mapear de forma plana, así que es normal si este se queda en null 
    // a menos que hagáis un mapeo personalizado más avanzado.
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_link() {
        return user_link;
    }

    public void setUser_link(String user_link) {
        this.user_link = user_link;
    }

    public String getPicture_link() {
        return picture_link;
    }

    public void setPicture_link(String picture_link) {
        this.picture_link = picture_link;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", user_link='" + user_link + '\'' +
                ", picture_link='" + picture_link + '\'' +
                '}';
    }

}
