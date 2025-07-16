package quiz.reward.com.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApiExceptionDto {

    private String errorCode;
    private String errorMsg;

}
