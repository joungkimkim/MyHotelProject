package com.shop.constant;

public enum ReservationStatus {

    OK("예약완료"),
    CANCEL("예약취소");


    private final String stringValue;

    // Enum 생성자
    private ReservationStatus(String value) {
        this.stringValue = value;
    }

    // 문자열 값을 반환하는 메서드
    public String getStringValue() {
        return stringValue;
    }


}
