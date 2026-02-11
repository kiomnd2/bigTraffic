package kr.kiomn2.bigtraffic.domain.accountbook.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteCategoryCommand {
    private final Long userId;
    private final Long categoryId;
}
