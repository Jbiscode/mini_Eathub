package com.eathub.mapper;

import com.eathub.dto.LoginDTO;
import com.eathub.dto.ReservationDTO;
import com.eathub.entity.Members;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
  Long getMemberSeqById(String member_id);
  Members selectMemberById(String member_id);
  void insertMember(Members memberJoinDTO);
  Members login(LoginDTO loginDTO);
  List<Members> selectMemberList();
  void update(Members memberUpdateDTO);
  void deleteMemberById(String member_id);
  void clearStore();

  List<ReservationDTO> selectReservationList(Long memSeq);

  List<ReservationDTO> selectReservationListPage(@Param("memberSeq") Long memberSeq, @Param("page")int page, @Param("type_tab") int type_tab);
}
