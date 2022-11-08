package org.keeper.object.payload;

public class IntPayLoad implements KeeperObjectPayload {
    private int value;

    public IntPayLoad() {
    }

    public IntPayLoad(int value) {
        this.value =value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntPayLoad{" +
                "value=" + value +
                '}';
    }
}
