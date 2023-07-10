package com.ekc.paymentservice.command.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment {

    @Id
    private String paymentId;
    private String orderId;
    private Date timeStamp;
    private String paymentStatus;

}
