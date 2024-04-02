package com.eathub.service;

import com.eathub.dto.LoginDTO;
import com.eathub.entity.Members;
import com.eathub.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public Members selectMemberById(String member_id) {
        return memberMapper.selectMemberById(member_id);
    }

    public  boolean isExist(String member_id) {
        return memberMapper.selectMemberById(member_id) != null;
    }

    public void insertMember(Members memberJoinDTO) {
        memberMapper.insertMember(memberJoinDTO);
    }

    public Members login(LoginDTO loginDTO, BindingResult bindingResult) {
        if (validateLoginInputs(loginDTO, bindingResult)) {
            return null;
        }
        return processLogin(loginDTO, bindingResult);
    }


    public void updateMember(Members memberUpdateDTO) {
        memberMapper.updateMember(memberUpdateDTO);
    }

    private boolean validateLoginInputs(LoginDTO loginDTO, BindingResult bindingResult) {
        if (!StringUtils.hasText(loginDTO.getMember_id())) {
            bindingResult.rejectValue("member_id", "required");
        }
        if (!StringUtils.hasText(loginDTO.getMember_pwd())) {
            bindingResult.rejectValue("member_pwd", "required");
        }
        return bindingResult.hasErrors();
    }
    private Members processLogin(LoginDTO loginDTO, BindingResult bindingResult) {
        Members loginMember = memberMapper.login(loginDTO);
        if (loginMember == null) {
            bindingResult.rejectValue("member_pwd", "incorrect");
        }
        return loginMember;
    }
}
