package by.may.application.service;

import by.may.application.dto.CategoryCreateRequest;
import by.may.application.mapper.CategoryMapper;
import by.may.application.port.CategoryRepositoryPort;
import by.may.application.port.EventPublisher;
import by.may.application.port.ProductRepositoryPort;
import by.may.domain.event.CategoryDeletedEvent;
import by.may.domain.exception.CategoryAlreadyExistsException;
import by.may.domain.exception.CategoryNotEmpty;
import by.may.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock private CategoryRepositoryPort categoryRepositoryPort;
    @Mock private CategoryMapper categoryMapper;
    @Mock private ProductRepositoryPort productRepositoryPort;
    @Mock private EventPublisher publisher;

    @InjectMocks private CategoryService categoryService;

    @Test
    void create_throwsWhenNameAlreadyExists() {
        CategoryCreateRequest dto = new CategoryCreateRequest("Техника", "desc");
        when(categoryRepositoryPort.existsByName("Техника")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createCategory(dto));
        verify(categoryRepositoryPort, never()).save(any());
    }

    @Test
    void create_savesCategoryWhenNameIsFree() {
        CategoryCreateRequest dto = new CategoryCreateRequest("Книги", "desc");
        Category category = new Category(null, "Книги", "desc");
        when(categoryRepositoryPort.existsByName("Книги")).thenReturn(false);
        when(categoryMapper.toModel(dto)).thenReturn(category);
        when(categoryRepositoryPort.save(category)).thenReturn(category);

        categoryService.createCategory(dto);

        verify(categoryRepositoryPort).save(category);
    }

    @Test
    void update_throwsWhenNewNameBelongsToAnotherCategory() {
        Category category = new Category(1L, "Техника", "desc");
        CategoryCreateRequest dto = new CategoryCreateRequest("Книги", "desc");
        when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepositoryPort.existsByName("Книги")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.updateCategory(1L, dto));
    }

    @Test
    void delete_throwsWhenCategoryHasProducts() {
        Category category = new Category(1L, "Техника", "desc");
        when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(category));
        when(productRepositoryPort.existsAnyByCategoryId(1L)).thenReturn(true);

        assertThrows(CategoryNotEmpty.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepositoryPort, never()).delete(any());
        verify(publisher, never()).publish(any());
    }

    @Test
    void delete_removesEmptyCategoryAndPublishesEvent() {
        Category category = new Category(1L, "Техника", "desc");
        when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(category));
        when(productRepositoryPort.existsAnyByCategoryId(1L)).thenReturn(false);

        categoryService.deleteCategory(1L);

        verify(categoryRepositoryPort).delete(1L);
        verify(publisher).publish(any(CategoryDeletedEvent.class));
    }
}