package by.may.web.controllers;

import by.may.application.dto.PriceHistoryResponse;
import by.may.application.dto.ProductCreateRequest;
import by.may.application.dto.ProductResponse;
import by.may.application.service.PriceHistoryService;
import by.may.application.service.ProductSearchCriteria;
import by.may.application.service.ProductService;
import by.may.application.service.ProductSortField;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/")
public class ProductController {

    private final ProductService productService;
    private final PriceHistoryService priceHistoryService;

    @GetMapping
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest dto) {
        return productService.createProduct(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Page<PriceHistoryResponse> getPriceHistory(@PathVariable Long id) {
        return priceHistoryService.getHistory(id);
    }

}
