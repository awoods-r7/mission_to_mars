package com.rapid7.awoods.mission_to_mars.GameComponents;

public class GameSession {

    private GameMode gameMode;

    public GameSession (){}

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
