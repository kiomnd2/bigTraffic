package kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {

    private String name;
    private String color;
    private String icon;
}
