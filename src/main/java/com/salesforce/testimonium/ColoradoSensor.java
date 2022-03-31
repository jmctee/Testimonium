package com.salesforce.testimonium;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This concrete implementation of a Sensor is intended to simulate an IRL sensor. For the purposes of this project,
 * assume that this class is actually communicating over a network to a physical sensor. Because sensors are physical
 * devices accessed via network communication, this class adds an artificial delay before returning the artificially
 * generated readings to simulate the real-world delays inherent in networks and physical equipment.
 */
public class ColoradoSensor implements Sensor {
    private final Integer NETWORK_DELAYS = 2000; // 2 seconds

    @Override
    public List<SensorReading> getReadings(LocalDateTime startTime, LocalDateTime endTime, Duration interval) {
        // Number of readings: Collection Window (endTime - startTime) / interval
        Long readingCount = Duration.between(startTime, endTime).toMillis() / interval.toMillis();

        // Fill the list with random values simulating actual sensor readings
        List<Double> values = new Random().doubles(readingCount, 0.01, 5.0).boxed().collect(Collectors.toList());

        List<SensorReading> readings = new ArrayList<>();

        LocalDateTime readingTime = startTime;

        values.stream().forEach(value -> {
            SensorReading reading = new SensorReading(readingTime, value, true);
            readings.add(reading);
            readingTime.plus(interval);
        });

        // Artificial delay simulates communication delays
        try {
            Thread.sleep(NETWORK_DELAYS);
        } catch (InterruptedException e) {
            //Ignore interrupts
        }

        return readings;
    }
}
