package com.ekc.commonservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderShippedEvent {

    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
