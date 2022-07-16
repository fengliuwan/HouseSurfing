package com.fengliuwan.staybooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity // used for ORM, mapping java class to table in DB
@Table(name = "user")
@JsonDeserialize(builder = User.Builder.class) // 用于把前端数据deserialize成java object
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    @JsonIgnore //不返回前端
    private String password;

    @JsonIgnore //不返回前端
    private boolean enabled; // used to disable/enable account

    public String getUsername() {
        return username;
    }

    /* 此处setter return type 为User 在从数据库中读取数据后给user赋值是被调用*/
    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    // constructor needed for builder pattern usage
    public User() {}

    private User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.enabled = builder.enabled;
    }

    public static class Builder {
        //把前端数据Json里面username 对应的 value 拿出来，spring调用setter 创建 builder object
        @JsonProperty("username")
        private String username;

        @JsonProperty("password")
        private String password;

        @JsonProperty("enabled")
        private boolean enabled;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
