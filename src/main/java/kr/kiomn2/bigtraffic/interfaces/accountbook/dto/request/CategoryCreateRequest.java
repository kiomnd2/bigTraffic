package kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest {

    private String name;
    private TransactionType type;
    private String color;
    private String icon;
}
