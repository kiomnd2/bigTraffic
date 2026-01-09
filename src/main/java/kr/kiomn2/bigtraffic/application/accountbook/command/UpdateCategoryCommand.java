package kr.kiomn2.bigtraffic.application.accountbook.command;

import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.CategoryUpdateRequest;
import lombok.Getter;

@Getter
public class UpdateCategoryCommand {
    private final Long userId;
    private final Long categoryId;
    private final String name;
    private final String color;
    private final String icon;

    private UpdateCategoryCommand(Long userId, Long categoryId, String name, String color, String icon) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public static UpdateCategoryCommand from(Long userId, Long categoryId, CategoryUpdateRequest request) {
        return new UpdateCategoryCommand(
                userId,
                categoryId,
                request.getName(),
                request.getColor(),
                request.getIcon()
        );
    }
}
