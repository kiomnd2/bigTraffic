package kr.kiomn2.bigtraffic.interfaces.accountbook.mapper;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(CategoryResponse::from)
                .toList();
    }
}
