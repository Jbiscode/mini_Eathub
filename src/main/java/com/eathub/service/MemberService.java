package com.eathub.service;

import com.eathub.dto.LoginDTO;
import com.eathub.dto.MemberJoinDTO;
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
    public void validateJoinInputs(MemberJoinDTO memberJoinDTO, BindingResult bindingResult) {
        if (!StringUtils.hasText(memberJoinDTO.getMember_id())) {
            log.error("member_id is empty");
            bindingResult.rejectValue("member_id", "required");
        }
        if (!StringUtils.hasText(memberJoinDTO.getMember_pwd())) {
            log.error("member_pwd is empty");
            bindingResult.rejectValue("member_pwd", "required");
        }
        if (!StringUtils.hasText(memberJoinDTO.getMember_name())) {
            log.error("member_name is empty");
            bindingResult.rejectValue("member_name", "required");
        }
        if (!StringUtils.hasText(memberJoinDTO.getMember_email())) {
            log.error("member_email is empty");
            bindingResult.rejectValue("member_email", "required");
        }
        if (!StringUtils.hasText(memberJoinDTO.getMember_phone())) {
            log.error("member_phone is empty");
            bindingResult.rejectValue("member_phone", "required");
        }
    }
}
