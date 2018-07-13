package be.kdg.se3.warmred.picker.domain;

import java.util.List;

/**
 * An interface that defines the optimization of a {@link CreateOrderMessage} based on the {@link PickingType} (SINGLE or GROUP)
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface PickingService {
    void optimize(CreateOrderMessage message);
    void optimize(List<CreateOrderMessage> messages);
}
