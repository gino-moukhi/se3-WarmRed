package be.kdg.se3.warmred.picker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * An class that optimizes the picking order of {@link CreateOrderMessage}
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class PickingImpl implements PickingService {
    private final Logger logger = LoggerFactory.getLogger(PickingImpl.class);

    /**
     * This method is called when the {@link PickingType} is SINGLE
     * @param message
     */
    @Override
    public void optimize(CreateOrderMessage message) {
        logger.info("locationinfo before sort: " + message.getLocationInfoList());
        message.getLocationInfoList().sort(Comparator.comparing(LocationInfo::getStorageRoom).thenComparing(LocationInfo::getHallway).thenComparing(LocationInfo::getRack));
        logger.info("locationinfo after sort: " + message.getLocationInfoList());
        List<Integer> orderedProductIds = new ArrayList<>();
        message.getLocationInfoList().forEach(item -> orderedProductIds.add(item.getProductID()));
        logger.info("items before sort: " + message.getItems());
        message.setItems(putItemsInCorrectOrder(message.getItems(), orderedProductIds));
        logger.info("items after sort: " + message.getItems());
    }

    /**
     * This method is called when the {@link PickingType} is GROUP
     * @param messages
     */
    @Override
    public void optimize(List<CreateOrderMessage> messages) {
        Collections.reverse(messages);
        messages.forEach(item -> item.getLocationInfoList().sort(Comparator.comparing(LocationInfo::getStorageRoom).thenComparing(LocationInfo::getHallway).thenComparing(LocationInfo::getRack)));
        for (CreateOrderMessage m : messages) {
            List<Integer> orderedProductIds = new ArrayList<>();
            m.getLocationInfoList().forEach(item -> orderedProductIds.add(item.getProductID()));
            m.setItems(putItemsInCorrectOrder(m.getItems(), orderedProductIds));
        }
    }

    /**
     * This private method uses the already optimized order of the location info to alter the entry's int the item map
     * so that both are in the same order
     * @param dataToSort
     * @param orderOfProductIds
     * @return
     */
    private Map<Integer, Integer> putItemsInCorrectOrder(Map<Integer, Integer> dataToSort, List<Integer> orderOfProductIds) {
        Map<Integer, Integer> sortedData = new LinkedHashMap<>();
        orderOfProductIds.forEach(id -> sortedData.put(id, dataToSort.get(id)));
        return sortedData;
    }
}
