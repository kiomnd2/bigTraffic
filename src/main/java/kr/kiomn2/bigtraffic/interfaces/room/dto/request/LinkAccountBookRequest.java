package kr.kiomn2.bigtraffic.interfaces.room.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkAccountBookRequest {

    @NotNull(message = "가계부 ID는 필수입니다.")
    private Long accountBookId;
}
