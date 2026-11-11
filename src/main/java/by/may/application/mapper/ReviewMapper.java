package by.may.application.mapper;

import by.may.application.dto.ReviewCreateRequest;
import by.may.application.dto.ReviewResponse;
import by.may.domain.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toModel(ReviewCreateRequest dto) {
        return new Review(
                dto.rating(),
                dto.comment(),
                dto.userId(),
                dto.productId()
        );
    }

    public ReviewResponse toDto(Review model){
        return new ReviewResponse(
                model.getId(),
                model.getRating(),
                model.getComment(),
                model.getUserId(),
                model.getProductId(),
                model.getCreatedAt()
        );
    }

}
