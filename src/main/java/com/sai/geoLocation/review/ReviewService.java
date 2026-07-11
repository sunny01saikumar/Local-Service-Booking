package com.sai.geoLocation.review;

import com.sai.geoLocation.entity.Review;
import com.sai.geoLocation.repository.ReviewRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class ReviewService extends CrudService<Review> {
    public ReviewService(ReviewRepository repository) {
        super(repository);
    }
}
