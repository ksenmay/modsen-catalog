package by.may.application.service;

import by.may.application.dto.ProductCreateRequest;
import by.may.application.port.EventPublisher;
import by.may.application.port.PriceHistoryRepositoryPort;
import by.may.application.port.ProductRepositoryPort;
import by.may.application.port.ReviewRepositoryPort;
import by.may.domain.event.PriceChangedEvent;
import by.may.domain.event.ProductDeletedEvent;
import by.may.domain.exception.ProductNotFoundException;
import by.may.domain.model.PriceHistory;
import by.may.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepositoryPort productRepositoryPort;
    @Mock private PriceHistoryRepositoryPort priceHistoryRepositoryPort;
    @Mock private ReviewRepositoryPort reviewRepositoryPort;
    @Mock private EventPublisher publisher;

    @InjectMocks private ProductService productService;

    @Test
    void update_withChangedPriceSavesHistoryAndPublishesEvent() {
        Product product = new Product(1L, "Ноутбук", "desc", new BigDecimal("100.00"), 1L, LocalDate.now(), 0.0);        ProductCreateRequest dto = new ProductCreateRequest("Ноутбук", "desc", new BigDecimal("120.00"), 1L);
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProduct(1L, dto);

        verify(priceHistoryRepositoryPort).save(any(PriceHistory.class));
        verify(publisher).publish(any(PriceChangedEvent.class));
    }

    @Test
    void update_withSamePriceDoesNotWriteHistory() {
        Product product = new Product(1L, "Ноутбук", "desc", new BigDecimal("100.00"), 1L, LocalDate.now(), 0.0);        ProductCreateRequest dto = new ProductCreateRequest("Ноутбук", "desc", new BigDecimal("100"), 1L);
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProduct(1L, dto);

        verify(priceHistoryRepositoryPort, never()).save(any());
        verify(publisher, never()).publish(any());
    }

    @Test
    void update_throwsWhenProductNotFound() {
        when(productRepositoryPort.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(9L, mock(ProductCreateRequest.class)));
    }

    @Test
    void delete_softDeletesReviewsBeforeProduct() {
        Product product = new Product(1L, "Ноутбук", "desc", new BigDecimal("100.00"), 1L, LocalDate.now(), 0.0);        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        InOrder order = inOrder(reviewRepositoryPort, productRepositoryPort);
        order.verify(reviewRepositoryPort).softDeleteByProductId(1L);
        order.verify(productRepositoryPort).delete(1L);
        verify(publisher).publish(any(ProductDeletedEvent.class));
    }

    @Test
    void findById_throwsWhenMissing() {
        when(productRepositoryPort.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(5L));
    }
}