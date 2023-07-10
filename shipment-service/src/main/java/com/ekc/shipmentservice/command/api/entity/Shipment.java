package com.ekc.shipmentservice.command.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shipment {

    @Id
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
