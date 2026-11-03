package by.may.infrastructure.mapper;

import by.may.domain.model.PriceHistory;
import by.may.infrastructure.entity.PriceHistoryJpaEntity;
import by.may.infrastructure.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PriceHistoryJpaMapper {

    public PriceHistory toModel(PriceHistoryJpaEntity entity) {
        return new PriceHistory(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getOldPrice(),
                entity.getNewPrice(),
                entity.getChangedAt()
        );
    }

    public PriceHistoryJpaEntity toEntity(PriceHistory model, ProductJpaEntity productJpa) {
        return new PriceHistoryJpaEntity(
                productJpa,
                model.getOldPrice(),
                model.getNewPrice()
        );
    }

}
