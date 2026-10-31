package by.may.application.service;

import by.may.application.dto.PriceHistoryResponse;
import by.may.application.mapper.PriceHistoryMapper;
import by.may.application.port.PriceHistoryRepositoryPort;
import by.may.application.service.query.PriceHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PriceHistoryService implements PriceHistoryQuery {
    private final PriceHistoryRepositoryPort priceHistoryRepositoryPort;
    private final PriceHistoryMapper priceHistoryMapper;

    @Transactional(readOnly = true)
    public Page<PriceHistoryResponse> getHistory(Long productId) {
        return priceHistoryRepositoryPort.findAllByProductId(productId).map(priceHistoryMapper::toDto);
    }

}
