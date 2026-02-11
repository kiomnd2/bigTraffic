package kr.kiomn2.bigtraffic.domain.accountbook.command;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCategoryCommand {
    private final Long userId;
    private final String name;
    private final TransactionType type;
    private final String color;
    private final String icon;
}
