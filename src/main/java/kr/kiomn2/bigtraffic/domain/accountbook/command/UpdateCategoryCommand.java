package kr.kiomn2.bigtraffic.domain.accountbook.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCategoryCommand {
    private final Long userId;
    private final Long categoryId;
    private final String name;
    private final String color;
    private final String icon;
}
