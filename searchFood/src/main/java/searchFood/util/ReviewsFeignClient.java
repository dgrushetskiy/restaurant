package searchFood.util;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import searchFood.model.ReviewRequest;
import searchFood.model.ReviewRequestItem;
import searchFood.model.ReviewResponseItem;

import java.util.List;

@FeignClient(name = "review", url = "localhost:9005/review", fallback = ReviewsFallback.class)
public interface ReviewsFeignClient {

    // Define a Feign client for making HTTP requests to the review service
    // The client is named "review" and communicates with the URL "localhost:9005/review"
    // If the request fails or the service is unavailable, it falls back to the ReviewsFallback class
    // to handle the request
    @RequestMapping(method = RequestMethod.POST, value = "/restaurantitem", consumes = "application/json")
    @Headers("Content-Type: application/json")
    List<ReviewResponseItem> fetchReviews(@RequestBody ReviewRequest request);
}
