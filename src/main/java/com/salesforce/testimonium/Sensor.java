package com.salesforce.testimonium;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface Sensor {
    public List<SensorReading> getReadings(LocalDateTime startTime, LocalDateTime endTime, Duration interval);
}
