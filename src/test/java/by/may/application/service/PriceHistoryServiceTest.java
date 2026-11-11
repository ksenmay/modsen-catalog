package by.may.application.service;

import by.may.application.dto.PriceHistoryResponse;
import by.may.application.mapper.PriceHistoryMapper;
import by.may.application.port.PriceHistoryRepositoryPort;
import by.may.domain.model.PriceHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceHistoryServiceTest {

    @Mock private PriceHistoryRepositoryPort priceHistoryRepositoryPort;
    @Mock private PriceHistoryMapper priceHistoryMapper;

    @InjectMocks private PriceHistoryService priceHistoryService;

    @Test
    void getHistory_returnsMappedPageFromRepository() {
        PriceHistory history = mock(PriceHistory.class);
        PriceHistoryResponse response = mock(PriceHistoryResponse.class);
        var pageable = PageRequest.of(0, 10);
        Page<PriceHistory> repositoryPage = new PageImpl<>(List.of(history), pageable, 1);

        when(priceHistoryRepositoryPort.findAllByProductId(5L, pageable)).thenReturn(repositoryPage);
        when(priceHistoryMapper.toDto(history)).thenReturn(response);

        Page<PriceHistoryResponse> result = priceHistoryService.getHistory(5L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().get(0));
        verify(priceHistoryRepositoryPort).findAllByProductId(5L, pageable);
    }

    @Test
    void getHistory_returnsEmptyPageWhenNoRecords() {
        var pageable = PageRequest.of(0, 10);
        Page<PriceHistory> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(priceHistoryRepositoryPort.findAllByProductId(7L, pageable)).thenReturn(emptyPage);

        Page<PriceHistoryResponse> result = priceHistoryService.getHistory(7L, pageable);

        assertTrue(result.isEmpty());
        verify(priceHistoryMapper, never()).toDto(any());
    }
}