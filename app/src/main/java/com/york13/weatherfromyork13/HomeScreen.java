package com.york13.weatherfromyork13;

public enum HomeScreen {
    FIRST(R.id.about_me), SECOND(R.id.contacts), THIRD(R.id.settings);

    HomeScreen(int itemId) {
        this.itemId = itemId;
    }

    private final int itemId;

    public int getItemId() {
        return itemId;
    }
}
