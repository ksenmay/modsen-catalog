package by.may.infrastructure.mapper;

import by.may.domain.model.Review;
import by.may.infrastructure.entity.ProductJpaEntity;
import by.may.infrastructure.entity.ReviewJpaEntity;
import by.may.infrastructure.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewJpaMapper {

    public Review toModel(ReviewJpaEntity entity) {
        return new Review(
                entity.getId(),
                entity.getRating(),
                entity.getComment(),
                entity.getUser().getId(),
                entity.getProduct().getId(),
                entity.getCreatedAt()
        );
    }

    public ReviewJpaEntity toNewEntity(Review model, ProductJpaEntity productJpa, UserJpaEntity userJpa) {
        return new ReviewJpaEntity(
                model.getRating(),
                model.getComment(),
                userJpa,
                productJpa
        );
    }

    public ReviewJpaEntity updateEntity(Review model, ReviewJpaEntity entity) {
        entity.setRating(model.getRating());
        entity.setComment(model.getComment());
        return entity;
    }
}
