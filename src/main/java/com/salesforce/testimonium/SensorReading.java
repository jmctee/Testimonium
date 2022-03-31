package com.salesforce.testimonium;

import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SensorReading {
    private LocalDateTime timestamp;
    private Double value;
    private Boolean valid;

    public SensorReading(LocalDateTime timestamp, Double value, Boolean valid) {
        this.timestamp = timestamp;
        this.value = value;
        this.valid = valid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Double getValue() {
        return value;
    }

    public Boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
