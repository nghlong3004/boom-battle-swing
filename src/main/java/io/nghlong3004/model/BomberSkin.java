package io.nghlong3004.model;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BomberSkin {
    BOZ(0, "boz"),
    EVIE(1, "evie"),
    IKE(2, "ike"),
    PLUNK(3, "plunk");

    public final int index;
    public final String name;

    public static BomberSkin SKIN = PLUNK;

}