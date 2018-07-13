package be.kdg.se3.warmred.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that uses a sleep function and keeps track of the amount of time slept (mainly used in the generator)
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
class Sleeper {
    private final Logger logger = LoggerFactory.getLogger(Sleeper.class);
    private int amountSleptInMilliseconds = 0;

    public void sleep(int duration) {
        try {
            amountSleptInMilliseconds += duration;
            Thread.sleep(duration);

        } catch (InterruptedException e) {
            logger.warn("Thread did not sleep for '" + duration + "' seconds");
        }
    }

    public int getAmountSleptInMilliseconds() {
        return amountSleptInMilliseconds;
    }

}
