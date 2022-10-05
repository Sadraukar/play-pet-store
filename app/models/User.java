package models;

import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
//  "user" is a reserved word in Postgres and should not be used as a table name
@Table(name="users")
public class User extends BaseModel {

    @Column(length = 255)
    @NotNull
    private String name;

    @Column(length = 255)
    @NotNull
    private String password;

    @Column(length = 255)
    @NotNull
    private String email;

    @NotNull
    private boolean isActive;

    public User(String name, String password, String email, boolean isActive) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.isActive = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
