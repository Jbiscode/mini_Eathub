package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {
    private String member_id;

    private String member_pwd;
    private String member_email;
    private String member_phone;
}
