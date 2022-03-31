package com.salesforce.testimonium;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsGenerator {
    public StatsSummary getStatsSummaryOverTimeInterval(LocalDateTime startTime, LocalDateTime endTime, Duration interval);
}
