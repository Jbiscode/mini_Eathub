package com.eathub.entity.ENUM;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MENU_TYPE {
    MAIN("MAIN"),
    SIDE("SIDE");

    private final String menu_type;
}
