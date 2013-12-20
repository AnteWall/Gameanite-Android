package com.gameanite.app.Containers;

/**
 * Created by Klante on 2013-12-20.
 */
public class GameRoomInfo {
    private String room;

    public GameRoomInfo(String name) {
        this.roomName = name;
    }

    public GameRoomInfo() {

    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    private String roomName;
}
