package com.bvc;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by bhavinchauhan on 4/18/16.
 */

public class DataPojo implements Comparator<DataPojo>, Comparable<DataPojo>{
    RGBColor type;
    Integer channelNumber;
    Integer index;
    Integer uniqueId;
    Long timestamp;

    public DataPojo(Long timestamp, RGBColor type, Integer channelNumber, Integer index, Integer uniqueId) {
        this.timestamp = timestamp;
        this.type = type;
        this.channelNumber = channelNumber;
        this.index = index;
        this.uniqueId = uniqueId;
    }

    public DataPojo(RGBColor type, Integer channelNumber, Integer index, Integer uniqueId) {
        this.type = type;
        this.index = index;
        this.channelNumber = channelNumber;
        this.uniqueId = uniqueId;
    }

    public Integer getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(Integer channelNumber) {
        this.channelNumber = channelNumber;
    }

    public RGBColor getType() {
        return type;
    }

    public void setType(RGBColor type) {
        this.type = type;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int compareTo(DataPojo o) {
        return compare(this, o);
    }

    @Override
    public int compare(DataPojo o1, DataPojo o2) {
        if (Objects.equals(o1.timestamp, o2.timestamp)) {
            return o1.getUniqueId() - o2.getUniqueId();
        }
        return (int) ((o1.timestamp - o2.timestamp));
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataPojo)) return false;

        DataPojo dataPojo = (DataPojo) o;

        return !(getUniqueId() != null ? !getUniqueId().equals(dataPojo.getUniqueId()) : dataPojo.getUniqueId() != null);

    }

    @Override
    public int hashCode() {
        return getUniqueId() != null ? getUniqueId().hashCode() : 0;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
