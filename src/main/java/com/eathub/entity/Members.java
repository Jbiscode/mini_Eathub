package com.eathub.entity;

import com.eathub.entity.ENUM.MEMBER_TYPE;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Members {
    private Long member_seq;
    private String member_id;
    private String member_pwd;
    private String member_name;
    private String member_email;
    private String member_phone;
    private MEMBER_TYPE member_type;
    private Date reg_date;
}
