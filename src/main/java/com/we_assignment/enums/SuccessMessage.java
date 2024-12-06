package com.we_assignment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
    CREATE("생성이 성공적으로 완료되었습니다."),
    UPDATE("수정이 성공적으로 완료되었습니다."),
    DELETE("삭제가 성공적으로 완료되었습니다.");

    private final String message;

}
