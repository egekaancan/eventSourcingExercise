package com.ekc.shipmentservice.command.api.repository;

import com.ekc.shipmentservice.command.api.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
