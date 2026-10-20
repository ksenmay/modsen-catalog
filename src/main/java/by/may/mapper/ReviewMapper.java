package by.may.mapper;

import by.may.dto.ReviewCreateRequest;
import by.may.dto.ReviewResponse;
import by.may.entity.Product;
import by.may.entity.Review;
import by.may.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewCreateRequest dto, User user, Product product) {
        return new Review(
                dto.rating(),
                dto.comment(),
                user,
                product
        );
    }

    public ReviewResponse toDto(Review review){
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getUser().getId(),
                review.getProduct().getId(),
                review.getCreatedAt()
        );
    }

}
