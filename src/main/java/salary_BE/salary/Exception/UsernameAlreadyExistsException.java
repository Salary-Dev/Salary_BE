package salary_BE.salary.Exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends RuntimeException {

    private final HttpStatus status;

    public UsernameAlreadyExistsException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT; // 기본 상태 코드 설정
    }

    public UsernameAlreadyExistsException(String message, HttpStatus status) {
        super(message);
        this.status = status; // 사용자 정의 상태 코드 설정
    }

    public HttpStatus getStatus() {
        return status;
    }
}


