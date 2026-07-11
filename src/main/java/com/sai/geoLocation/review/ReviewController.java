package com.sai.geoLocation.review;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.Review;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController extends CrudController<Review> {
    public ReviewController(ReviewService service) {
        super(service);
    }
}
