package be.kdg.se3.warmred.picker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for scheduling runnable objects like the clearing of a cache or the retry of the unprocessed orders
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class TaskScheduler {
    private final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    public TaskScheduler() {
    }

    public void scheduleAtFixedRate(Runnable runnable, int delay) {
        logger.info("Running runnable " + runnable + " at a fixed rate of " + delay + " seconds");
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, delay, TimeUnit.SECONDS);
    }
}
