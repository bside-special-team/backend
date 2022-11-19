package com.beside.special.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UserLevelTest {

    @ParameterizedTest
    @CsvSource(value = {
        "0,외지인",
        "10,외지인",
        "49,외지인",
        "50,동네주민",
        "999,동네주민",
        "1000,동네이장",
        "2999,동네이장",
        "3000,동네군수",
        "5999,동네군수",
        "6000,동네통장",
        "7000,동네통장",
    })
    void 포인트에_맞는_레벨을_리턴한다(int point, String label) {
        UserLevel userLevel = UserLevel.findByPoint(point);

        assertThat(userLevel.getLabel()).isEqualTo(label);
    }
}
