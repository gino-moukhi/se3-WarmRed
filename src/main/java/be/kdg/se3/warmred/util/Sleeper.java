package be.kdg.se3.warmred.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sleeper {
    private final Logger logger = LoggerFactory.getLogger(Sleeper.class);
    private int amountSleptInMiliseconds = 0;

    public void sleep(int duration) {
        try {
            amountSleptInMiliseconds += duration;
            Thread.sleep(duration);

        } catch (InterruptedException e) {
            logger.warn("Thread did not sleep for '" + duration + "' seconds");
        }
    }

    public int getAmountSleptInMiliseconds() {
        return amountSleptInMiliseconds;
    }

}
