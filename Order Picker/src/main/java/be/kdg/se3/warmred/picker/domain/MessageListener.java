package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.domain.dto.Dto;

public interface MessageListener {
    void onReceive(Dto dtoMessage);
}
