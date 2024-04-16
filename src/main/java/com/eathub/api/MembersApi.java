package com.eathub.api;

import com.eathub.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MembersApi {
    private final MemberService memberService;

    @PostMapping("/isexistid")
    public Map<String, Boolean> isExist(@RequestBody Map<String, String> request) {
        String memberId = request.get("memberId");
        boolean exists = memberService.isExist(memberId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return response;
    }
    //검색 결과 페이지에서 모달창의 확인 버튼을 누를 때마다 session에 저장시킵니다.
    @PostMapping("/putWantingDetails")
    public String putWantingDetails(@RequestBody Map<String, String> request, HttpSession session) {
        String wantingDate = request.get("date");
        String wantingHour = request.get("hour");
        String wantingPerson = request.get("person");

        session.setAttribute("wantingDate", wantingDate);
        session.setAttribute("wantingHour", wantingHour);
        session.setAttribute("wantingPerson", wantingPerson);
        return wantingDate + wantingHour + wantingPerson;
    }
    // 전에 선택했던 날짜, 인원수, 시간을 session으로부터 가져옵니다.
    @PostMapping("/getWantingDetails")
    public Map<String, String> getWantingDetails(HttpSession session) {
        Map<String, String> wantingDetails = new HashMap<>();
        wantingDetails.put("wantingDate", (String) session.getAttribute("wantingDate"));
        wantingDetails.put("wantingHour", (String) session.getAttribute("wantingHour"));
        wantingDetails.put("wantingPerson", (String) session.getAttribute("wantingPerson"));
        return wantingDetails;
    }

}
