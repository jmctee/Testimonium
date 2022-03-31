package com.salesforce.testimonium;

import org.junit.BeforeClass;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.easymock.EasyMock.contains;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import static org.junit.Assert.*;

@RunWith(EasyMockRunner.class)
public class TestColoradoStatsGenerator {
    @Mock
    private static Sensor sensor;

    private static StatsGenerator statsGenerator;

    @BeforeClass
    public static void setup() {
        statsGenerator = new ColoradoStatsGenerator(sensor);
    }

    @Test
    public void testSimpleSunnyDay() throws Exception {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        Duration interval = Duration.ofSeconds(5);

        // Because sensor readings are non-deterministic (we model this using random values),
        // everytime this call is made,the results will be different...
        StatsSummary statsSummary = statsGenerator.getStatsSummaryOverTimeInterval(startTime, endTime, interval);

        assertNotNull(statsSummary);

//        assertEquals(expectedStats.getMean(), statsSummary.getMean(), 0.0001);
//        assertEquals(expectedStats.getMedian(), statsSummary.getMedian(), 0.0001);
//        assertEquals(expectedStats.getStdDeviation(), statsSummary.getStdDeviation(), 0.0001);
//        assertEquals(expectedStats.getPercentReadingsInError(), statsSummary.getPercentReadingsInError(), 0.0001);
    }
}
