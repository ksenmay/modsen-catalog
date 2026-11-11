package by.may.web.controllers;

import by.may.application.dto.PriceHistoryResponse;
import by.may.application.dto.ProductCreateRequest;
import by.may.application.dto.ProductResponse;
import by.may.application.service.PriceHistoryService;
import by.may.application.service.ProductSearchCriteria;
import by.may.application.service.ProductService;
import by.may.application.service.ProductSortField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name="Продукты", description="Каталог продуктов с поиском, фильтрацией сортировкой")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final PriceHistoryService priceHistoryService;

    @Operation(summary = "Поиск продуктов",
            description = "Фильтрация, сортировка, поиск по названию")
    @GetMapping
    @SecurityRequirements
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) ProductSortField sortBy,
            @RequestParam(defaultValue = "true") boolean asc,
            @PageableDefault(size = 10) Pageable pageable) {

        ProductSearchCriteria criteria = new ProductSearchCriteria(
                name, categoryId, minPrice, maxPrice, minRating, sortBy, asc);
        return productService.searchProducts(criteria, pageable);
    }

    @Operation(summary = "Продукт по id",
            description = "Доступно всем")
    @SecurityRequirements
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @Operation(summary = "Создать продукт",
            description = "Доступно ADMIN и MANAGER")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest dto) {
        return productService.createProduct(dto);
    }

    @Operation(summary = "Изменить продукт",
            description = "Доступно ADMIN и MANAGER")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest dto) {
        return productService.updateProduct(id, dto);
    }

    @Operation(summary = "Удалить продукт",
            description = "Доступно ADMIN и MANAGER")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @Operation(summary = "История цен продукта",
            description = "Доступно всем")
    @SecurityRequirements
    @GetMapping("/{id}/price-history")
    @ResponseStatus(HttpStatus.OK)
    public Page<PriceHistoryResponse> getPriceHistory(@PathVariable Long id,
                                                      @PageableDefault(
                                                              size = 10,
                                                              sort = "changedAt",
                                                              direction = Sort.Direction.DESC)
                                                      Pageable pageable) {
        return priceHistoryService.getHistory(id, pageable);
    }

}
