package com.eathub.dto;

import com.eathub.entity.ENUM.MEMBER_TYPE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDTO {

  private String member_id;
  private String member_pwd;
  private String member_name;
  private String member_email;
  private String member_phone;
  private final MEMBER_TYPE member_type;

  public MemberJoinDTO(MEMBER_TYPE member_type) {
    this.member_type = member_type;
  }
}
