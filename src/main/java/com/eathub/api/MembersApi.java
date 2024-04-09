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

    @PostMapping("/wantingDetails")
    public void wantingDetails(@RequestBody Map<String, String> request, HttpSession session) {
        String wantingDate = request.get("date");
        String wantingHour = request.get("hour");
        String wantingPerson = request.get("person");

        session.setAttribute("wantingDate", wantingDate);
        session.setAttribute("wantingHour", wantingHour);
        session.setAttribute("wantingPerson", wantingPerson);

    }
}
