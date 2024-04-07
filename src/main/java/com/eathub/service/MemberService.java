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

    // member_id로 회원정보 가져오기
    public Members selectMemberById(String member_id) {
        return memberMapper.selectMemberById(member_id);
    }

    // 회원가입시 member_id 중복체크
    public  boolean isExist(String member_id) {
        return memberMapper.selectMemberById(member_id) != null;
    }

    // 회원가입
    public void insertMember(Members memberJoinDTO) {
        memberMapper.insertMember(memberJoinDTO);
    }

    // 로그인
    public Members login(LoginDTO loginDTO, BindingResult bindingResult) {
        if (validateLoginInputs(loginDTO, bindingResult)) {
            return null;
        }
        return processLogin(loginDTO, bindingResult);
    }

    // 회원정보 수정
    public void updateMember(Members memberUpdateDTO) {
        memberMapper.updateMember(memberUpdateDTO);
    }

    // 로그인시 아이디, 비밀번호 체크
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
