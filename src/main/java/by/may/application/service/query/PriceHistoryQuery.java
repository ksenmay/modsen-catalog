package by.may.application.service.query;

import by.may.application.dto.PriceHistoryResponse;
import org.springframework.data.domain.Page;

public interface PriceHistoryQuery {
    Page<PriceHistoryResponse> getHistory(Long productId);
}
