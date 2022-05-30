package ro.pub.cs.systems.eim.practicaltest02;

public class BitcoinData {
    private String updated;
    private String value;
    private long currentTime;

    public BitcoinData(String updated, String value, long currentTime) {
        this.updated = updated;
        this.value = value;
        this.currentTime = currentTime;
    }

    public BitcoinData() {}

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "BitcoinResults{" +
                "rate='" + value + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
