package com.esotericsoftware.kryonet.examples.chat;

public class Input {
    public enum Dir { UP, DOWN, LEFT, RIGHT };
    final public Dir dir;

    public Input(Dir dir) {
        this.dir = dir;
    }
}