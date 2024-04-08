package com.eathub.dto;

import com.eathub.entity.ENUM.MENU_TYPE;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


@Getter
@Setter
@ToString
public class MenuFormDTO {
    private Long menu_seq;
    private Long restaurant_seq;
    private String menu_name;
    private String menu_info;
    private CommonsMultipartFile menu_image;
    private String menu_image_name;
    private String menu_image_path;
    private String  menu_price;
    private MENU_TYPE menu_type;

}
