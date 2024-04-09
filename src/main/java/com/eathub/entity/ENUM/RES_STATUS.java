package com.eathub.entity.ENUM;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RES_STATUS {

        STANDBY("STANDBY")
        , ACCESS("ACCESS")
        , OK("OK")
        , REJECT("REJECT");

        private final String res_status;


}
