package by.may.infrastructure.adapter;

import by.may.application.port.ReviewRepositoryPort;
import by.may.domain.exception.ProductNotFoundException;
import by.may.domain.exception.ReviewNotFoundException;
import by.may.domain.exception.UserNotFoundException;
import by.may.domain.model.Review;
import by.may.infrastructure.entity.ProductJpaEntity;
import by.may.infrastructure.entity.ReviewJpaEntity;
import by.may.infrastructure.entity.UserJpaEntity;
import by.may.infrastructure.mapper.ReviewJpaMapper;
import by.may.infrastructure.repository.ProductRepository;
import by.may.infrastructure.repository.ReviewRepository;
import by.may.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewAdapter implements ReviewRepositoryPort {

    private final ReviewRepository repository;
    private final ReviewJpaMapper jpaMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Review save(Review review) {
        ProductJpaEntity productJpaEntity = productRepository.findById(review.getProductId()).orElseThrow(
                () -> new ProductNotFoundException(review.getProductId())
        );
        UserJpaEntity userJpaEntity = userRepository.findById(review.getUserId()).orElseThrow(
                () -> new UserNotFoundException(review.getUserId())
        );
        return jpaMapper.toModel(repository.save(jpaMapper.toNewEntity(review, productJpaEntity, userJpaEntity)));
    }

    @Override
    public void delete(Long id) {
        ReviewJpaEntity entity = repository.findById(id).orElseThrow(
                () -> new ReviewNotFoundException(id)
        );
        repository.delete(entity);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id).map(jpaMapper::toModel);
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        return repository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public void softDeleteByProductId(Long productId) {
        repository.softDeleteByProductId(productId);
    }

    @Override
    public Page<Review> findAllByProductId(Long productId, Pageable pageable) {
        return repository.findAllByProductId(productId, pageable).map(jpaMapper::toModel);
    }

    @Override
    public double calculateAverageRating(Long productId) {
        return repository.calculateAverageRating(productId);
    }
}
