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

@FeignClient(name = "review", url = "localhost:9005/review")
public interface ReviewsFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/restaurantitem", consumes = "application/json")
    @Headers("Content-Type: application/json")
    List<ReviewResponseItem> fetchReviews(@RequestBody ReviewRequest request) ;
    //ResponseEntity<Object> fetchReviews(@RequestBody ReviewRequest request) ;



    @RequestMapping(method = RequestMethod.GET, value = "/test", consumes = "application/json")
    @Headers("Content-Type: application/json")
    String fetchTestReviews() ;
}
