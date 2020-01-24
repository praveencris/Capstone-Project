package com.sabkayar.praveen.takeorderdistribute.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"userName"},
        unique = true)})
public class UserName {
    @PrimaryKey(autoGenerate = true)
    private int userId;

    @NonNull
    private String userName;

    public UserName(@NonNull String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }


}
