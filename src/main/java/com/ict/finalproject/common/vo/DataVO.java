package com.ict.finalproject.common.vo;

import lombok.Getter;

/*
 공통 응답 객체
 모든 Controller는 이 DataVO를 통해 응답
 프론트엔드와의 응답 형식을 통일하기 위한 목적
*/

/*
 @Getter 설명
 - Lombok이 컴파일 시점에 모든 필드에 대한 getter 메서드를 자동 생성
 - getSuccess(), getData(), getMessage() 메서드를 직접 작성하지 않아도 된다
 - DataVO는 "응답 전용 객체"이므로 setter를 열어둘 필요가 없고 불변 구조가 더 안전하다
*/
@Getter
public class DataVO<T> {

    private final boolean success;
    private final T data;
    private final String message;

    public DataVO(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    /* 성공 응답 */
    public static <T> DataVO<T> success(T data) {
        return new DataVO<>(true, data, null);
    }

    /* 성공 응답 메시지 포함 */
    public static <T> DataVO<T> success(T data, String message) {
        return new DataVO<>(true, data, message);
    }

    /* 실패 응답 */
    public static <T> DataVO<T> fail(String message) {
        return new DataVO<>(false, null, message);
    }
}
