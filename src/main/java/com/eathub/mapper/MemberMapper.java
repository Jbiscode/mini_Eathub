package com.eathub.mapper;

import com.eathub.dto.LoginDTO;
import com.eathub.entity.Members;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
  Long getMemberSeqById(String member_id);
  Members selectMemberById(String member_id);
  void insertMember(Members memberJoinDTO);
  Members login(LoginDTO loginDTO);
  List<Members> selectMemberList();
  void updateMember(Members memberUpdateDTO);
  void deleteMemberById(String member_id);
  void clearStore();
}
