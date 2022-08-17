package model;

import java.util.HashMap;
import java.util.Map;

public enum RightClickStatus {
    BLANK(0) /*= 0*/, FLAG(1) /*= 1*/, QMARK(2);/*= 2*/

    private int number;

    private RightClickStatus(int number) {
        this.number = number;
    }

    public int getNum() {
        return number;
    }

    private static Map<Integer, RightClickStatus> numberToEnumMap = new HashMap<>();

    static {
        for (RightClickStatus rightClickStatus : RightClickStatus.values()) {
            numberToEnumMap.put(rightClickStatus.getNum(), rightClickStatus);
        }
    }

    public static RightClickStatus intToEnum(int number) {
        return numberToEnumMap.get(number);
    }
}
