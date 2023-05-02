package com.audiophileproject.services;
import com.audiophileproject.proxies.UserProxy;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentService {
    @Value("${stripe.keys.secret}")
    private String API_SECRET_KEY;
    private final UserProxy userProxy;

    @PostConstruct
    public void init(){
        Stripe.apiKey = this.API_SECRET_KEY;
    }

    public Integer getLimitByUserId(String userId)  {
        Map<String, Object> options = new HashMap<>();
        String email = userProxy.getUserDetailsByUserId(userId).getEmail();
        options.put("email", email);
        try {
            // retrieve the customer from the email
            List<Customer> customers = Customer.list(options).getData();
            System.out.println("customers "+customers);
            if (customers.size() > 0) {
                Customer customer = customers.get(0);
                System.out.println("target customer "+customer);
                Map<String, Object> subscriptionFilter = new HashMap<>();

                // retrieve customer's subcription
                subscriptionFilter.put("customer",customer.getId());
                List<Subscription> subscriptions = Subscription.list(subscriptionFilter).getData();
                Subscription subscription = subscriptions.get(0);
                System.out.println("target customer subscription "+subscription);

                String productId = subscription.getItems().getData().get(0).getPrice().getProduct();


                String limitString = Product.retrieve(productId).getMetadata().get("limit");
                System.out.println("limit of customer "+customer.getEmail()+" IS "+limitString);
                return Integer.parseInt(limitString);
            }

        } catch (StripeException e) {
            e.printStackTrace();
            throw new RuntimeException("stripe exception: couldn't fetch customer "+email+"'s limit");
        }

        throw new RuntimeException("couldn't fetch customer "+email+"'s limit");
    }
}
