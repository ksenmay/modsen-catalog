package by.may.controllers;

import by.may.dto.ProductCreateRequest;
import by.may.dto.ProductResponse;
import by.may.service.ProductSearchCriteria;
import by.may.service.ProductService;
import by.may.service.ProductSortField;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
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
    public ProductResponse getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest dto) {
        return productService.createProduct(dto);
    }

    @PutMapping
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
