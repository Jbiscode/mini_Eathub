package com.eathub.api;

import com.eathub.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
