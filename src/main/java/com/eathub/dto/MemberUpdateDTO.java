package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberUpdateDTO {
    private String member_id;
    @NotEmpty
    private String member_name;
    @NotEmpty
    private String member_name;
    @NotEmpty
    private String member_pwd;
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",message = "이메일 형식이 아닙니다.")
    private String member_email;
    @NotEmpty
    private String member_phone;
}
