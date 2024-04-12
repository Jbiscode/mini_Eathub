package com.eathub.service;

import com.eathub.dto.LoginDTO;
import com.eathub.dto.ReservationDTO;
import com.eathub.entity.Members;
import com.eathub.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    /**
        * 로그인을 수행하는 메서드입니다.
        *
        * @param loginDTO 로그인 정보를 담고 있는 LoginDTO 객체
        * @param bindingResult 유효성 검사 결과를 담고 있는 BindingResult 객체
        * @return 로그인에 성공한 경우 Members 객체를 반환하고, 실패한 경우 null을 반환합니다.
        */
    public Members login(LoginDTO loginDTO, BindingResult bindingResult) {
        if (validateLoginInputs(loginDTO, bindingResult)) {
            return null;
        }
        return processLogin(loginDTO, bindingResult);
    }

    // 회원정보 수정
    public void update(Members memberUpdateDTO) {
        memberMapper.update(memberUpdateDTO);
    }

    // 로그인시 아이디, 비밀번호 체크
    /**
     * 로그인 입력값을 유효성 검사하는 메서드입니다.
     * 
     * @param loginDTO 로그인 정보를 담고 있는 LoginDTO 객체
     * @param bindingResult 유효성 검사 결과를 담고 있는 BindingResult 객체
     * @return 유효성 검사 결과를 반환합니다. 유효성 검사에 실패하면 true를 반환하고, 성공하면 false를 반환합니다.
     */
    private boolean validateLoginInputs(LoginDTO loginDTO, BindingResult bindingResult) {
        if (!StringUtils.hasText(loginDTO.getMember_id())) {
            bindingResult.rejectValue("member_id", "required");
        }
        if (!StringUtils.hasText(loginDTO.getMember_pwd())) {
            bindingResult.rejectValue("member_pwd", "required");
        }
        return bindingResult.hasErrors();
    }
    /**
     * Members에 대한 설명입니다.
     *
     * @param loginDTO 로그인 정보를 담고 있는 LoginDTO 객체
     * @param bindingResult 유효성 검사 결과를 담고 있는 BindingResult 객체
     * @return 로그인에 성공한 Members 객체, 실패 시 null을 반환합니다.
     */
    private Members processLogin(LoginDTO loginDTO, BindingResult bindingResult) {
        Members loginMember = memberMapper.login(loginDTO);
        if (loginMember == null) {
            bindingResult.rejectValue("member_pwd", "incorrect");
        }
        return loginMember;
    }

    public List<ReservationDTO> getReservationList(Long memSeq) {
       List<ReservationDTO> reservationList =  memberMapper.selectReservationList(memSeq);

        // Date Format / D-day  넣기
        for (ReservationDTO reservationDTO : reservationList) {
            Date date = reservationDTO.getRes_date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d (E)").withLocale(Locale.KOREA);
            String formattedDate = localDate.format(formatter);

            reservationDTO.setDateFormat(formattedDate);

            // D-day 계산
            Date today = new Date();
            Date reservationDate = reservationDTO.getRes_date();


            Calendar calendarToday = Calendar.getInstance();
            calendarToday.setTime(today);

            calendarToday.set(Calendar.HOUR_OF_DAY, 0);
            calendarToday.set(Calendar.MINUTE, 0);
            calendarToday.set(Calendar.SECOND, 0);
            calendarToday.set(Calendar.MILLISECOND, 0);


            Calendar calendarReservation = Calendar.getInstance();
            calendarReservation.setTime(reservationDate);

            calendarReservation.set(Calendar.HOUR_OF_DAY, 0);
            calendarReservation.set(Calendar.MINUTE, 0);
            calendarReservation.set(Calendar.SECOND, 0);
            calendarReservation.set(Calendar.MILLISECOND, 0);


            long diff = calendarReservation.getTimeInMillis() - calendarToday.getTimeInMillis();
            long dDay = TimeUnit.MILLISECONDS.toDays(diff);
            long absDday = Math.abs(dDay);

            reservationDTO.setAbsDday(absDday);
            reservationDTO.setDDay(dDay);
        }

        return reservationList;
    }

    public List<String> convertStringToList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String[] elements = input.split(",");
        return Arrays.asList(elements);
    }


}
