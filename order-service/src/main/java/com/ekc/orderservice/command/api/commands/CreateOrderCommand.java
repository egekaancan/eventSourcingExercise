package com.ekc.orderservice.command.api.commands;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private int quantity;
    private String orderStatus;
}
