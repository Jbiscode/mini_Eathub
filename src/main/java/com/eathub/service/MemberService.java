package com.eathub.service;

import com.eathub.dto.LoginDTO;
import com.eathub.dto.MemberDTO;
import com.eathub.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberDTO selectMemberById(String member_id) {
        return memberMapper.selectMemberById(member_id);
    }

    public  boolean isExist(String member_id) {
        return memberMapper.selectMemberById(member_id) != null;
    }

    public void insertMember(MemberDTO memberDTO) {
        memberMapper.insertMember(memberDTO);
    }

    public MemberDTO login(LoginDTO loginDTO, BindingResult bindingResult) {
        if (!StringUtils.hasText(loginDTO.getMember_id())) {
            bindingResult.rejectValue("member_id", "required");
        }
        if (!StringUtils.hasText(loginDTO.getMember_pwd())) {
            bindingResult.rejectValue("member_pwd", "required");
        }

        if (bindingResult.hasErrors()) {
            return null;
        }

        MemberDTO loginMember = memberMapper.login(loginDTO);

        if (loginMember == null) {
            bindingResult.rejectValue("member_pwd", "incorrect");
            return null;
        }

        return loginMember;
    }

    public void updateMember(String member_id, MemberDTO memberDTO) {
        memberMapper.updateMember(member_id, memberDTO);
    }

}
