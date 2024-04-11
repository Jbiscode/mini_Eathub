package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.ReservationDTO;
import com.eathub.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/mydining")
@RequiredArgsConstructor
public class MyDiningController {

    private final MemberService memberService;

    @ModelAttribute("page")
    public String page() {
        return "mydining";
    }
    @GetMapping("")
    public String myDining(Model model, HttpSession session) {
        // 로그인회원의 예약목록 불러오기위해서 session에 있는 memseq 가져오기
        Long mem_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        log.info("mem_seq={}", mem_seq);

        // mem_seq로 예약상태 리스트 불러오기 (reservation Service? 일단 memberservice에)
        List<ReservationDTO> reservationList =  memberService.getReservationList(mem_seq);

        // Date Format / D-day  넣기
        for (ReservationDTO reservationDTO : reservationList) {
            Date date = reservationDTO.getRes_date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d (E)").withLocale(Locale.KOREA);
            String formattedDate = localDate.format(formatter);

            reservationDTO.setDateFormat(formattedDate);

            // D-day 계산
            Date today = new Date();
            long diff = reservationDTO.getRes_date().getTime() - today.getTime();
            long dDay = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            long absDday = Math.abs(dDay);

            reservationDTO.setAbsDday(absDday);
            reservationDTO.setDDay(dDay);
        }

        model.addAttribute("reservationDTOList", reservationList);


        return "/members/myDining";
    }
}
