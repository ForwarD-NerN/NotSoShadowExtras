package ru.nern.notsoshadowextras.crash_fix;
public enum UpdateSuppressionReason {
    SO("StackOverflow"),
    CCE("CCE"),
    OOM("OOM"),
    SOUND("Sound"),
    UNKNOWN("");

    private final String name;

    UpdateSuppressionReason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
