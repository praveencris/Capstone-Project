package com.sabkayar.praveen.takeorderdistribute.takeOrder;

import com.sabkayar.praveen.takeorderdistribute.takeOrder.model.ItemInfo;

import java.util.ArrayList;

public final class Utils {
    public static ArrayList<ItemInfo> getDummyList() {
        ArrayList<ItemInfo> arrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setChecked(false);
            itemInfo.setItemName("Item " + (i + 1));
            itemInfo.setItemPrice("");
            itemInfo.setMaxItemsAllowed(4);
            arrayList.add(itemInfo);
        }
        return arrayList;
    }
}
