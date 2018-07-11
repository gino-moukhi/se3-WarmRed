package be.kdg.se3.warmred.picker.domain;

import java.util.List;

public interface PickingService {
    CreateOrderMessage optimize(CreateOrderMessage message) throws PickingException;
    List<CreateOrderMessage> optimize(List<CreateOrderMessage> messages) throws PickingException;
}
