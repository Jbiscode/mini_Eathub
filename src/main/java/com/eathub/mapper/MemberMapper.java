package com.eathub.mapper;

import com.eathub.dto.LoginDTO;
import com.eathub.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
  MemberDTO selectMemberById(String member_id);
  void insertMember(MemberDTO memberDTO);
  MemberDTO login(LoginDTO loginDTO);
  List<MemberDTO> selectMemberList();
  void updateMember(String member_id, MemberDTO memberDTO);
  void deleteMemberById(String member_id);
  void clearStore();
}
