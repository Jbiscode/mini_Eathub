package com.eathub.dto;

import com.eathub.type.MEMBER_TYPE;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {

  private Long member_seq;
  private String member_id;
  private String member_pwd;
  private String member_name;
  private String member_email;
  private String member_phone;
  private MEMBER_TYPE member_type;
  private Date reg_date;

  public MemberDTO(MEMBER_TYPE member_type) {
    this.member_type = member_type;
  }

  public MemberDTO(
    String member_id,
    String member_pwd,
    String member_name,
    String member_email,
    String member_phone,
    MEMBER_TYPE member_type
  ) {
    this.member_id = member_id;
    this.member_pwd = member_pwd;
    this.member_name = member_name;
    this.member_email = member_email;
    this.member_phone = member_phone;
    this.member_type = member_type;
  }
}
