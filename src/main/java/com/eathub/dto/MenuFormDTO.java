package com.eathub.dto;

import com.eathub.entity.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuFormDTO {
    private List<Menu> menuList = new ArrayList<>();
}
