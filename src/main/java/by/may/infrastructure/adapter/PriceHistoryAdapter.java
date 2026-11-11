package by.may.infrastructure.adapter;

import by.may.application.port.PriceHistoryRepositoryPort;
import by.may.domain.exception.ProductNotFoundException;
import by.may.domain.model.PriceHistory;
import by.may.infrastructure.entity.ProductJpaEntity;
import by.may.infrastructure.mapper.PriceHistoryJpaMapper;
import by.may.infrastructure.repository.PriceHistoryRepository;
import by.may.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceHistoryAdapter implements PriceHistoryRepositoryPort {
    private final PriceHistoryRepository repository;
    private final PriceHistoryJpaMapper jpaMapper;
    private final ProductRepository productRepository;

    @Override
    public PriceHistory save(PriceHistory priceHistory) {
        ProductJpaEntity product = productRepository.findById(priceHistory.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(priceHistory.getProductId())
                );
        return jpaMapper.toModel(repository.save(jpaMapper.toEntity(priceHistory, product)));
    }

    @Override
    public Page<PriceHistory> findAllByProductId(Long productId, Pageable pageable) {
        return repository.findAllByProductId(productId, pageable).map(jpaMapper::toModel);
    }

}
