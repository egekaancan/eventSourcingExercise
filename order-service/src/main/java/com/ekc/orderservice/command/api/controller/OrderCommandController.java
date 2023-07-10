package com.ekc.orderservice.command.api.controller;

import com.ekc.orderservice.command.api.commands.CreateOrderCommand;
import com.ekc.orderservice.command.api.model.OrderRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {

        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand command = CreateOrderCommand.builder()
                .orderId(orderId)
                .addressId(orderRestModel.getAddressId())
                .productId(orderRestModel.getProductId())
                .quantity(orderRestModel.getQuantity())
                .orderStatus("CREATED")
                .build();

        commandGateway.sendAndWait(command);

        return "Order Created!";
    }
}
