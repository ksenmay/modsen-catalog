package by.may.application.mapper;

import by.may.application.dto.PriceHistoryResponse;
import by.may.domain.model.PriceHistory;
import org.springframework.stereotype.Component;

@Component
public class PriceHistoryMapper {

    public PriceHistoryResponse toDto(PriceHistory model) {
        return new PriceHistoryResponse(
                model.getId(),
                model.getProductId(),
                model.getOldPrice(),
                model.getNewPrice(),
                model.getChangedAt());
    }
    
}
