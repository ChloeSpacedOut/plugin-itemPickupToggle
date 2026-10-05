package com.choespacedout.itemPickupToggle;

public class PlayerConfig {
    private boolean itemPickupToggle;

    public PlayerConfig(boolean newItemPickupToggle) {
        itemPickupToggle = newItemPickupToggle;
    }

    public boolean getItemPickupToggle() {
        return itemPickupToggle;
    }

    public void setItemPickupToggle(boolean toggle) {
        itemPickupToggle = toggle;
    }


}
