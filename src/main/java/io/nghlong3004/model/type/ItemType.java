package io.nghlong3004.model.type;

import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public enum ItemType {
    BLANK(-1),
    ITEM_BOMB(0),
    ITEM_BOMB_SIZE(1),
    ITEM_SHOE(2);

    public final int id;
    private static final Random random = new Random();

    public static ItemType random() {
        ItemType[] values = values();
        return values[random.nextInt(values.length)];
    }
}
