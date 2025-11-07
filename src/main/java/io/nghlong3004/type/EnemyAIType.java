package io.nghlong3004.type;

public enum EnemyAIType {
    EASY(0, 0.8f),
    NORMAL(1, 1.0f),
    HARD(2, 1f);

    public final int id;
    public final float speedMultiplier;

    EnemyAIType(int id, float speedMultiplier) {
        this.id = id;
        this.speedMultiplier = speedMultiplier;
    }
}
