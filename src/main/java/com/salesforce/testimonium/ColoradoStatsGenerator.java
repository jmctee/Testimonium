package com.salesforce.testimonium;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class ColoradoStatsGenerator implements StatsGenerator {
    private Sensor sensor;

    public ColoradoStatsGenerator(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public StatsSummary getStatsSummaryOverTimeInterval(LocalDateTime startTime, LocalDateTime endTime, Duration interval) {
        List<SensorReading> readings = sensor.getReadings(startTime, endTime, interval);

        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

        readings.stream().filter(reading -> reading.isValid()).forEach(reading -> {
            descriptiveStatistics.addValue(reading.getValue());
        });

        Long invalidCount = readings.stream().filter(reading -> !reading.isValid()).count();

        double mean = descriptiveStatistics.getMean();
        double median = descriptiveStatistics.getPercentile(50);
        double stdDeviation = descriptiveStatistics.getStandardDeviation();
        double percentReadingsInError = invalidCount.doubleValue() / readings.size();

        StatsSummary summary = new StatsSummary(readings, mean, median, stdDeviation, percentReadingsInError);

        return summary;
    }
}
