package com.eathub.dto;

import com.eathub.entity.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuFormDTOWrapper {
    private List<MenuFormDTO> menuList;
    public MenuFormDTOWrapper() {
        this.menuList = new ArrayList<>();
    }
}
