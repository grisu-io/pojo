package io.grisu.pojo.supportingclasses;

import java.util.Date;
import java.util.UUID;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class UserTestPojo extends AbstractPojo {

    @Property(name = "uuid")
    private UUID uuid;

    @Property(name = "first_name")
    private String firstName;

    @Property(name = "last_name")
    private String lastName;

    @Property(name = "created_at")
    private Date createdAt;

    @Property(name = "telegram_id")
    private String telegramId;

    @Property(name = "encrypted_password")
    private String encryptedPassword;

    @Property(name = "status")
    private Integer status;

    public String getFirstName() {
        return firstName;
    }

    public UserTestPojo setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserTestPojo setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public UserTestPojo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public UserTestPojo setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UserTestPojo setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UserTestPojo setUuid(java.util.UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public UserTestPojo setTelegramId(String telegramId) {
        this.telegramId = telegramId;
        return this;
    }
}
