package kr.kiomn2.bigtraffic.application.accountbook.command;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.CategoryCreateRequest;
import lombok.Getter;

@Getter
public class CreateCategoryCommand {
    private final Long userId;
    private final String name;
    private final TransactionType type;
    private final String color;
    private final String icon;

    private CreateCategoryCommand(Long userId, String name, TransactionType type, String color, String icon) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.color = color;
        this.icon = icon;
    }

    public static CreateCategoryCommand from(Long userId, CategoryCreateRequest request) {
        return new CreateCategoryCommand(
                userId,
                request.getName(),
                request.getType(),
                request.getColor(),
                request.getIcon()
        );
    }
}
