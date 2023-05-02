package com.audiophileproject.controllers;

import com.audiophileproject.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    // This is your Stripe CLI webhook secret for testing your endpoint locally.
    String endpointSecret = "whsec_205eb93fa3fb0fb0985c5aef090e9776bbb119f7ca7e763a438e4f4af34fb5a1";

    @GetMapping(value = "/limit")
    public Map<String,Object> getUserLimit(@RequestParam String userId){
        Map<String, Object> res = new HashMap<>();
        Integer limit = paymentService.getLimitByUserId(userId);
        res.put("userId", userId);
        res.put("limit",limit);
        return res;
    }

//            @PostMapping("/webhook")
//            public String post(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader, HttpServletRequest request, HttpServletResponse response){
//                Stripe.apiKey = "sk_test_51N0LFTKpaH2wiwbE3wgdmZGRokZcERXtqMfIB9mmGJYXJwFtho8D7Aoqgl95OOQ6jJMMhHCydbjyf4yRCGnfPd2700ZIJnW9dj";
////                String payload = request.body();
////                String sigHeader = request.headers("Stripe-Signature");
//                Event event = null;
//
//                try {
//                    event = Webhook.constructEvent(
//                            payload, sigHeader, endpointSecret
//                    );
//                } catch (JsonSyntaxException e) {
//                    // Invalid payload
//                    response.setStatus(400);
//                    return "";
//                } catch (SignatureVerificationException e) {
//                    // Invalid signature
//                    response.setStatus(400);
//                    return "";
//                }
//
//                // Deserialize the nested object inside the event
//                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//                StripeObject stripeObject = null;
//                if (dataObjectDeserializer.getObject().isPresent()) {
//                    stripeObject = dataObjectDeserializer.getObject().get();
//                } else {
//                    // Deserialization failed, probably due to an API version mismatch.
//                    // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
//                    // instructions on how to handle this case, or return an error here.
//                }
//                // Handle the event
//                System.out.println("EVENT TYPE ---> "+event.getType());
//                switch (event.getType()) {
//                    case "payment_intent.succeeded": {
//                        System.out.println("PAYMENT INTENT SUCCEEDED"+event);
//                        // Then define and call a function to handle the event payment_intent.succeeded
//                        break;
//                    }
//                    case "product.created":{
//                        Product product = (Product)(event.getDataObjectDeserializer().getObject().orElseThrow(()->new RuntimeException("Error getting product details")));
//
//                        Plan plan = new Plan();
//                        plan.setId(product.getId());
//                        plan.setName(product.getName());
//                        plan.setDescription(product.getDescription());
//                        plan.setLimitGB(Integer.parseInt(product.getMetadata().get("limit")));
//                        System.out.println("CUSTOMER CREATED "+ plan);
//                        paymentService.createPlan(plan);
//                        break;
//                    }
//                    case "product.updated":{
//                        System.out.println("PRODUCT UPDATED "+event);
//                        Product product = (Product)(event.getDataObjectDeserializer().getObject().orElseThrow(()->new RuntimeException("Error getting product details")));
//                        Plan plan = new Plan();
//                        plan.setId(product.getId());
//                        plan.setName(product.getName());
//                        plan.setDescription(product.getDescription());
//                        plan.setLimitGB(Integer.parseInt(product.getMetadata().get("limit")));
//                        System.out.println("SUBSCRIPTION UPDATED "+ plan);
//                        paymentService.updatePlan(plan);
//                        break;
//                    }
//                    case "customer.created":{
//                        System.out.println("CUSTOMER CREATED"+event);
//                        Customer customer = (Customer)(event.getDataObjectDeserializer().getObject().orElseThrow(()->new RuntimeException("Error getting product details")));
//                        Customer_ customer_ = new Customer_();
//                        customer_.setId(customer.getId());
//                        customer_.setName(customer.getName());
//                        customer_.setEmail(customer.getEmail());
//                        customer_.setUsername(customer.getEmail());
//                        System.out.println("SUBSCRIBER CREATED "+customer_);
//                        paymentService.createCustomer(customer_);
//                        break;
//                    }
//                    case "customer.subscription.created":{
//                        System.out.println("SUBSCRIPTION CREATED"+event);
//                        Subscription subscription = (Subscription)(event.getDataObjectDeserializer().getObject().orElseThrow(()->new RuntimeException("Error getting product details")));
//                        Subscription_ subscription_ = new Subscription_();
//                        subscription_.setPlanId(subscription.getItems().getData().get(0).getPrice().getProduct());
//                        subscription_.setCustomerId(subscription.getCustomer());
//                        System.out.println("SUBSCRIPTION CREATED "+subscription_);
//                        paymentService.createSubscription(subscription_);
//                        break;
//                    }
//                    // ... handle other event types
//                    default:
//                        System.out.println("Unhandled event type: " + event.getType());
//                }
//
//                response.setStatus(200);
//                return "";
//            };
}

