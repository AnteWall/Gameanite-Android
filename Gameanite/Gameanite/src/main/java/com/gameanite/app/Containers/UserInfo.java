package com.gameanite.app.Containers;

/**
 * Created by Klante on 2013-12-20.
 */
public class UserInfo {

    private String userName;
    private String color;
    private String room;

    public String getRoomSocket() {
        return roomSocket;
    }

    public void setRoomSocket(String roomSocket) {
        this.roomSocket = roomSocket;
    }

    private String roomSocket;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
