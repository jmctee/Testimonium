package com.salesforce.testimonium;

import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

import static org.junit.Assert.*;

public class TestColoradoStatsGenerator {
    private static StatsGenerator statsGenerator;

    @BeforeClass
    public static void setup() {
        statsGenerator = new ColoradoStatsGenerator();
    }

    @Test
    public void testStuff() throws Exception {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        Duration interval = Duration.ofSeconds(5);

        // Because sensor readings are non-deterministic (we model this using random values),
        // everytime this call is made,the results will be different...
        StatsSummary statsSummary = statsGenerator.getStatsSummaryOverTimeInterval(startTime, endTime, interval);

        assertNotNull(statsSummary);

        // ...so we have to recalculate the stats every time and have no control over what we get.
        //
        // This also makes it hard to test edge cases
        StatsSummary expectedStats = generateStatsFromReadings(statsSummary.getReadings());

        assertEquals(expectedStats.getMean(), statsSummary.getMean(), 0.0001);
        assertEquals(expectedStats.getMedian(), statsSummary.getMedian(), 0.0001);
        assertEquals(expectedStats.getStdDeviation(), statsSummary.getStdDeviation(), 0.0001);
        assertEquals(expectedStats.getPercentReadingsInError(), statsSummary.getPercentReadingsInError(), 0.0001);
    }

    private StatsSummary generateStatsFromReadings(List<SensorReading> readings) {
        List<Double> values = new ArrayList<>();
        readings.stream().filter(reading -> reading.isValid()).forEach(reading -> {
            values.add(reading.getValue());
        });

        Long invalidCount = readings.stream().filter(reading -> !reading.isValid()).count();
        Double mean = calculateMean(values);
        Double median = calculateMedian(values);
        Double stdDeviation = calculateStdDeviation(values);
        Double percentReadingsInError = invalidCount.doubleValue() / readings.size();
        StatsSummary summary = new StatsSummary(readings, mean, median, stdDeviation, percentReadingsInError);

        return summary;
    }

    private Double calculateMean(List<Double> values) {
        Double mean = values.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
        return mean;
    }

    private Double calculateMedian(List<Double> values) {
        DoubleStream sortedValues = values.stream().mapToDouble(Double::doubleValue).sorted();
        Double median = values.size()%2 == 0?
                sortedValues.skip(values.size()/2-1).limit(2).average().getAsDouble():
                sortedValues.skip(values.size()/2).findFirst().getAsDouble();
        return median;
    }

    private Double calculateStdDeviation(List<Double> values) {
        Double mean = calculateMean(values);
        Double rawSum = values.stream().mapToDouble(value -> Math.pow(value - mean, 2)).sum();
        Double stdDeviation = Math.sqrt(rawSum/values.size());
        return stdDeviation;
    }
}
