package com.salesforce.testimonium;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class StatsSummary {
    private List<SensorReading> readings;
    private Double mean;
    private Double median;
    private Double stdDeviation;
    private Double percentReadingsInError;

    public StatsSummary(List<SensorReading> readings, Double mean, Double median, Double stdDeviation, Double percentReadingsInError) {
        this.readings = readings;
        this.mean = mean;
        this.median = median;
        this.stdDeviation = stdDeviation;
        this.percentReadingsInError = percentReadingsInError;
    }

    public List<SensorReading> getReadings() {
        return readings;
    }

    public Double getMean() {
        return mean;
    }

    public Double getMedian() {
        return median;
    }

    public Double getStdDeviation() {
        return stdDeviation;
    }

    public Double getPercentReadingsInError() {
        return percentReadingsInError;
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
