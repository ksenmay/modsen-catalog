package by.may.application.service.query;

import by.may.application.dto.PriceHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PriceHistoryQuery {
    Page<PriceHistoryResponse> getHistory(Long productId, Pageable pageable);
}
