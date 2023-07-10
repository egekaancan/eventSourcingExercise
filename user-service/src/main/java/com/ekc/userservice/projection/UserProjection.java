package com.ekc.userservice.projection;

import com.ekc.commonservice.model.CardDetails;
import com.ekc.commonservice.model.User;
import com.ekc.commonservice.queries.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {

        //Change the logic to get details from DB
        CardDetails cardDetails = CardDetails.builder()
                .name("Ege Kaan Can")
                .cardNumber("12345")
                .validUntilMonth(8)
                .validUntilYear(2025)
                .cvv(800)
                .build();
        return User.builder()
                .userId(query.getUserId())
                .firstName("Ege Kaan")
                .lastName("Can")
                .cardDetails(cardDetails)
                .build();
    }
}
