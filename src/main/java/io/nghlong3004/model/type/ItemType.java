package io.nghlong3004.model.type;

import java.util.Random;

public enum ItemType {
    ITEM_BOMB(0),      // Tăng số lượng bomb có thể đặt
    ITEM_BOMB_SIZE(1), // Tăng phạm vi nổ của bomb
    ITEM_SHOE(2);      // Tăng tốc độ di chuyển

    public final int id;
    private static final Random random = new Random();

    ItemType(int id) {
        this.id = id;
    }

    public static ItemType random() {
        ItemType[] values = values();
        return values[random.nextInt(values.length)];
    }
}
