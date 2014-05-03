package com.esotericsoftware.kryonet.examples.chat;

public class ServerInput {
    public enum Dir { UP, DOWN, LEFT, RIGHT };
    final public Dir dir;

    public ServerInput(Dir dir) {
        this.dir = dir;
    }
}