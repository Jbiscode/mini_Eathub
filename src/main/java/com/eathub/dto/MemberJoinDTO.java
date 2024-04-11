package com.eathub.dto;

import com.eathub.entity.ENUM.MEMBER_TYPE;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class MemberJoinDTO {

    private final MEMBER_TYPE member_type;
    @NotEmpty
    private String member_id;
    @NotEmpty
    private String member_pwd;
    @NotEmpty
    private String member_name;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",message = "이메일 형식이 아닙니다.")
    private String member_email;

    @NotEmpty
    private String member_phone;

    public MemberJoinDTO(MEMBER_TYPE member_type) {
        this.member_type = member_type;
    }
}
