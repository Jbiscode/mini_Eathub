package com.eathub.controller;

import com.eathub.dto.LoginDTO;
import com.eathub.dto.MemberDTO;
import com.eathub.service.MemberService;
import com.eathub.type.MEMBER_TYPE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        log.info("login 페이지 이동");
        return "/members/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO, BindingResult bindingResult) {
        log.info("로그인 시도");
        MemberDTO loginMember = memberService.login(loginDTO , bindingResult);

        if (loginMember == null) {
            log.error("로그인 실패");
            return "/members/loginForm";
        }
        return "/index";
    }


    /**
     * 회원가입 종류 선택
     *
     * @return
     */
    @GetMapping("/join/select")
    public String login() {
        return "/members/joinSelect";
    }


    /**
     * 고객 회원가입 폼
     *
     * @param model
     * @return
     */
    @GetMapping("/join/customer")
    public String joinCustomer(Model model) {
        model.addAttribute("memberDTO", new MemberDTO(MEMBER_TYPE.CUSTOMER));
        return "/members/joinForm";
    }

    @PostMapping("/join/customer")
    public String joinCustomer(@ModelAttribute MemberDTO memberDTO, BindingResult bindingResult, Model model) {

        if (!StringUtils.hasText(memberDTO.getMember_id())) {
            log.error("아이디가 없습니다.");
            bindingResult.rejectValue("member_id", "required");
        }
        if (!StringUtils.hasText(memberDTO.getMember_pwd())) {
            log.error("비밀번호가 없습니다.");
            bindingResult.rejectValue("member_pwd", "required");
        }

        if (bindingResult.hasErrors()) {
            return "/members/joinForm";
        }
        memberService.insertMember(memberDTO);

        model.addAttribute("loginDTO", new LoginDTO(memberDTO.getMember_id(), ""));
        return "/members/loginForm";
    }


    /**
     * 사장님 회원가입 폼
     *
     * @param model
     * @return
     */
    @GetMapping("/join/owner")
    public String joinOwner(Model model) {
        model.addAttribute("memberDTO", new MemberDTO(MEMBER_TYPE.OWNER));
        return "/members/joinForm";
    }

    @PostMapping("/join/owner")
    public String joinOwner(MemberDTO memberDTO, BindingResult bindingResult) {
        if (!StringUtils.hasText(memberDTO.getMember_id())) {
            bindingResult.rejectValue("member_id", "required");
        }
        if (!StringUtils.hasText(memberDTO.getMember_pwd())) {
            bindingResult.rejectValue("member_pwd", "required");
        }

        memberService.insertMember(memberDTO);
        return "/members/loginForm";
    }

}
