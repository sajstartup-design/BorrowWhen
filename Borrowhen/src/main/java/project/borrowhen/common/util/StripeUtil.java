package project.borrowhen.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class StripeUtil {

    @Value("${stripe.api.secret.key}")
    private String apiSecretKey;
    
    @Value("${stripe.api.publishable.key}")
    private String apiPublishableKey;

    public static String STRIPE_API_SECRET_KEY;
    
    public static String STRIPE_API_PUBLISHABLE_KEY;
    
    @PostConstruct
    public void init() {
    	STRIPE_API_SECRET_KEY = apiSecretKey;
    	STRIPE_API_PUBLISHABLE_KEY = apiPublishableKey;
    }
}