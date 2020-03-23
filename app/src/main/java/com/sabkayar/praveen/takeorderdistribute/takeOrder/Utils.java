package com.sabkayar.praveen.takeorderdistribute.takeOrder;




import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item;

import java.util.ArrayList;

public final class Utils {
    public static ArrayList<Item> getDummyList() {
        ArrayList<Item> arrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Item itemInfo = new Item("Item " + (i + 1), "Item " + (i + 1), "0", 4);
            arrayList.add(itemInfo);
        }
        return arrayList;
    }
}
