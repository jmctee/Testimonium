package com.salesforce.testimonium;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(EasyMockRunner.class)
public class TestColoradoStatsGenerator {
    @Mock
    private static Sensor sensor;

    private static StatsGenerator statsGenerator;

    /**
     * This method is the actual piece of code that executes a test. All of the actual tests simply set up the desired
     * date and provide the expected results.
     *
     * @param readings - the readings the sensor mock should return
     * @param startTime - The reading start time
     * @param endTime - The reading end time
     * @param interval - The desired time between readings
     * @param expectedMean - Given the supplied readings, the expected mean to be returned by the stats generator
     * @param expectedMedian - Given the supplied readings, the expected median to be returned by the stats generator
     * @param expectedStdDeviation - Given the supplied readings, the expected standard deviation to be returned by
     *                               the stats generator
     * @param expectedPercentReadingsInError - Given the supplied readings, the expected percent readings in error to
     *                                         be returned by the stats generator
     *
     * @throws Exception
     */
    private void executeTest(List<SensorReading> readings,
                             LocalDateTime startTime, LocalDateTime endTime, Duration interval,
                             Double expectedMean, Double expectedMedian,
                             Double expectedStdDeviation, Double expectedPercentReadingsInError) throws Exception {

        /**
         * The EasyMock library makes it easy to simulate behaviors during testing for components that are not part of
         * the component under test (in this case Sensor).
         *
         * Note that instead of creating an instance of sensor, EasyMock creates a "virtual copy" of the class
         *
         * The following code says that when getReadings is called on the virtual copy, it should return the supplied
         * readings.
         *
         * The replay call just tells the mock to prepare for use.
         */
        expect(sensor.getReadings(startTime, endTime, interval)).andReturn(readings);
        replay(sensor);

        /**
         * Note that nowhere in these tests is this Sensor#getReadings method called, it happens inside of the stats
         * generator class. So we need to supply our mocked sensor to the stats generator so that the desired data
         * is returned from the call.
         *
         * This technique is often called Dependency Injection (DI). The stats generator depends on a sensor to get its
         * data. Rather than letting the class create its dependency, it is "injected" into the class. From the
         * classes viewpoint, it does not have any knowledge of the Sensor class being injected other than the contract
         * outlined in the interface.
         *
         * There are several strategies and tools for DI, this one is called constructor injection and useful for simple
         * cases like this.
         */
        StatsGenerator statsGenerator = new ColoradoStatsGenerator(sensor);

        StatsSummary statsSummary = statsGenerator.getStatsSummaryOverTimeInterval(startTime, endTime, interval);

        assertNotNull(statsSummary);

        assertEquals(expectedMean, statsSummary.getMean(), 0.0001);
        assertEquals(expectedMedian, statsSummary.getMedian(), 0.0001);
        assertEquals(expectedStdDeviation, statsSummary.getStdDeviation(), 0.0001);
        assertEquals(expectedPercentReadingsInError, statsSummary.getPercentReadingsInError(), 0.0001);
    }

    /**
     * A test of well-formed data where all readings are valid
     */
    @Test
    public void testSimpleSunnyDay() throws Exception {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        Duration interval = Duration.ofSeconds(5);

        List<SensorReading> readings = Arrays.asList(new SensorReading(startTime, 1.0, true),
                new SensorReading(startTime.plus(interval), 2.0, true),
                new SensorReading(startTime.plus(interval.multipliedBy(2)), 3.0, true),
                new SensorReading(startTime.plus(interval.multipliedBy(3)), 4.0, true),
                new SensorReading(startTime.plus(interval.multipliedBy(4)), 5.0, true));

        Double expectedMean = 3.0;
        Double expectedMedian = 3.0;
        Double expectedStdDeviation = 1.5811;
        Double expectedPercentReadingsInError = 0.0;

        executeTest(readings, startTime, endTime, interval, expectedMean, expectedMedian, expectedStdDeviation, expectedPercentReadingsInError);
    }

    /**
     * A test of well-formed data where some readings are invalid
     */
    @Test
    public void testInvalidReadings() throws Exception {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        Duration interval = Duration.ofSeconds(5);

        List<SensorReading> readings = Arrays.asList(new SensorReading(startTime, 1.0, true),
                new SensorReading(startTime.plus(interval), 2.0, true),
                new SensorReading(startTime.plus(interval.multipliedBy(2)), 3.0, false),
                new SensorReading(startTime.plus(interval.multipliedBy(3)), 4.0, false),
                new SensorReading(startTime.plus(interval.multipliedBy(4)), 5.0, true));

        Double expectedMean = 2.6666;
        Double expectedMedian = 2.0;
        Double expectedStdDeviation = 2.0816;
        Double expectedPercentReadingsInError = 0.4;

        executeTest(readings, startTime, endTime, interval, expectedMean, expectedMedian, expectedStdDeviation, expectedPercentReadingsInError);
    }

    /**
     * A test of well-formed data where all readings are invalid
     */
    @Test
    public void testAllInvalidReadings() throws Exception {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        Duration interval = Duration.ofSeconds(5);

        List<SensorReading> readings = Arrays.asList(new SensorReading(startTime, 1.0, false),
                new SensorReading(startTime.plus(interval), 2.0, false),
                new SensorReading(startTime.plus(interval.multipliedBy(2)), 3.0, false),
                new SensorReading(startTime.plus(interval.multipliedBy(3)), 4.0, false),
                new SensorReading(startTime.plus(interval.multipliedBy(4)), 5.0, false));

        Double expectedMean = Double.NaN;
        Double expectedMedian = Double.NaN;
        Double expectedStdDeviation = Double.NaN;
        Double expectedPercentReadingsInError = 1.0;

        executeTest(readings, startTime, endTime, interval, expectedMean, expectedMedian, expectedStdDeviation, expectedPercentReadingsInError);
    }

    /**
     * A test where no readings are returned
     */
    @Test
    public void testEmptyReadings() throws Exception {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        Duration interval = Duration.ofSeconds(5);

        List<SensorReading> readings = new ArrayList<>();

        Double expectedMean = Double.NaN;
        Double expectedMedian = Double.NaN;
        Double expectedStdDeviation = Double.NaN;
        Double expectedPercentReadingsInError = Double.NaN;

        executeTest(readings, startTime, endTime, interval, expectedMean, expectedMedian, expectedStdDeviation, expectedPercentReadingsInError);
    }
}
