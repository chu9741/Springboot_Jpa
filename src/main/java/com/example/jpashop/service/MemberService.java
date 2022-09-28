package com.example.jpashop.service;

import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    //@Autowired
    //private MemberRepository memberRepository;

    private final MemberRepository memberRepository;

//    @Autowired
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository=memberRepository;
//    }

    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); //중복회원검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers= memberRepository.findAllByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("No member exists."));
    }
}
