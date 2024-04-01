package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.LoginDTO;
import com.eathub.dto.MemberJoinDTO;
import com.eathub.dto.MemberUpdateDTO;
import com.eathub.entity.ENUM.MEMBER_TYPE;
import com.eathub.entity.Members;
import com.eathub.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @ModelAttribute("page")
    public String page() {
        return "members";
    }

    @GetMapping("/my")
    public String myPage(MemberJoinDTO memberJoinDTO, Model model) {
        model.addAttribute("memberJoinDTO", memberJoinDTO);
        return "/members/myPage";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        model.addAttribute("loginDTO", new LoginDTO());
        log.info("login 페이지 이동");
        return "/members/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        log.info("로그인 시도");
        Members loginMember = memberService.login(loginDTO, bindingResult);

        if (loginMember == null) {
            log.error("로그인 실패");
            return "/members/loginForm";
        }

        HttpSession session = request.getSession();
        // 세션에 로그인 정보 저장
        session.setAttribute(SessionConf.LOGIN_MEMBER, loginMember.getMember_id());
        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
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
        model.addAttribute("memberJoinDTO", new MemberJoinDTO(MEMBER_TYPE.CUSTOMER));
        return "/members/joinForm";
    }

    @PostMapping("/join/customer")
    public String joinCustomer(@ModelAttribute MemberJoinDTO memberJoinDTO, BindingResult bindingResult, Model model) {

        memberService.validateJoinInputs(memberJoinDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/members/joinForm";
        }
        memberService.insertMember(
                Members.builder()
                        .member_id(memberJoinDTO.getMember_id())
                        .member_pwd(memberJoinDTO.getMember_pwd())
                        .member_name(memberJoinDTO.getMember_name())
                        .member_email(memberJoinDTO.getMember_email())
                        .member_phone(memberJoinDTO.getMember_phone())
                        .member_type(MEMBER_TYPE.CUSTOMER)
                        .build()
        );

        model.addAttribute("loginDTO", new LoginDTO(memberJoinDTO.getMember_id(), ""));
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
        model.addAttribute("memberJoinDTO", new MemberJoinDTO(MEMBER_TYPE.OWNER));
        return "/members/joinForm";
    }

    @PostMapping("/join/owner")
    public String joinOwner(MemberJoinDTO memberJoinDTO, BindingResult bindingResult) {

        memberService.validateJoinInputs(memberJoinDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/members/joinForm";
        }
        memberService.insertMember(
                Members.builder()
                        .member_id(memberJoinDTO.getMember_id())
                        .member_pwd(memberJoinDTO.getMember_pwd())
                        .member_name(memberJoinDTO.getMember_name())
                        .member_email(memberJoinDTO.getMember_email())
                        .member_phone(memberJoinDTO.getMember_phone())
                        .member_type(MEMBER_TYPE.OWNER)
                        .build()
        );
        return "/members/loginForm";
    }

    @GetMapping("/update")
    public String updatePage(Model model, HttpSession session) {
        Members member = memberService.selectMemberById((String) session.getAttribute(SessionConf.LOGIN_MEMBER));
        model.addAttribute("memberUpdateDTO", member);
        return "/members/updateForm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberUpdateDTO memberUpdateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/members/updateForm";
        }
        memberService.updateMember(
                Members.builder()
                        .member_id(memberUpdateDTO.getMember_id())
                        .member_pwd(memberUpdateDTO.getMember_pwd())
                        .member_email(memberUpdateDTO.getMember_email())
                        .member_phone(memberUpdateDTO.getMember_phone())
                        .build()
        );
        return "redirect:/members/my";
    }

}
