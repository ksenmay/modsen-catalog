package by.may.application.port;

import by.may.domain.model.PriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface PriceHistoryRepositoryPort {
    PriceHistory save(PriceHistory priceHistory);
    Page<PriceHistory> findAllByProductId(Long productId);
    void softDeleteByProductId(Long productId);
}
