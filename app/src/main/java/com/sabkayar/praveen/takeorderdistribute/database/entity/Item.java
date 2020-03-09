package com.sabkayar.praveen.takeorderdistribute.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

@OnConflictStrategy()
@Entity(indices = {@Index(value = {"itemName"},
        unique = true)})
public class Item implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int itemId;

    @NonNull
    private String itemName;

    @ColumnInfo(defaultValue = "0")
    private int itemPrice;

    protected Item(Parcel in) {
        itemId = in.readInt();
        itemName = in.readString();
        itemPrice = in.readInt();
        maxItemAllowed = in.readInt();
        isChecked = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(@NonNull String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setMaxItemAllowed(int maxItemAllowed) {
        this.maxItemAllowed = maxItemAllowed;
    }

    @ColumnInfo(defaultValue = "4")
    private int maxItemAllowed;

    @ColumnInfo(defaultValue = "0")
    private int isChecked;


    @Ignore
    public Item(@NonNull String itemName, int itemPrice, int maxItemAllowed, int isChecked) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
        this.isChecked = isChecked;
    }

    public Item(int itemId, @NonNull String itemName, int itemPrice, int maxItemAllowed, int isChecked) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
        this.isChecked = isChecked;
    }

    public int getItemId() {
        return itemId;
    }

    @NonNull
    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getMaxItemAllowed() {
        return maxItemAllowed;
    }


    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(itemName);
        dest.writeInt(itemPrice);
        dest.writeInt(maxItemAllowed);
        dest.writeInt(isChecked);
    }
}
