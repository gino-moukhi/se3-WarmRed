package be.kdg.se3.warmred.picker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PickingImpl implements PickingService {
    private Logger logger = LoggerFactory.getLogger(PickingImpl.class);

    @Override
    public CreateOrderMessage optimize(CreateOrderMessage message) throws PickingException {
        logger.info("locationinfo before sort: " + message.getLocationInfoList());
        message.getLocationInfoList().sort(Comparator.comparing(LocationInfo::getStorageRoom).thenComparing(LocationInfo::getHallway).thenComparing(LocationInfo::getRack));
        logger.info("locationinfo after sort: " + message.getLocationInfoList());
        List<Integer> orderedProductIds = new ArrayList<>();
        message.getLocationInfoList().forEach(item -> orderedProductIds.add(item.getProductID()));
        logger.info("items before sort: " + message.getItems());
        message.setItems(putItemsInCorrectOrder(message.getItems(), orderedProductIds));
        logger.info("items after sort: " + message.getItems());
        return message;
    }

    @Override
    public List<CreateOrderMessage> optimize(List<CreateOrderMessage> messages) throws PickingException {
        Collections.reverse(messages);
        messages.forEach(item -> item.getLocationInfoList().sort(Comparator.comparing(LocationInfo::getStorageRoom).thenComparing(LocationInfo::getHallway).thenComparing(LocationInfo::getRack)));
        for (CreateOrderMessage m : messages) {
            List<Integer> orderedProductIds = new ArrayList<>();
            m.getLocationInfoList().forEach(item -> orderedProductIds.add(item.getProductID()));
            m.setItems(putItemsInCorrectOrder(m.getItems(), orderedProductIds));
        }
        return messages;
    }

    private Map<Integer, Integer> putItemsInCorrectOrder(Map<Integer, Integer> dataToSort, List<Integer> orderOfProductIds) {
        Map<Integer, Integer> sortedData = new LinkedHashMap<>();
        orderOfProductIds.forEach(id -> sortedData.put(id, dataToSort.get(id)));
        return sortedData;
    }
}
