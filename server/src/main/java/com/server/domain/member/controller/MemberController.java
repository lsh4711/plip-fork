package com.server.domain.member.controller;

import java.net.URI;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.domain.mail.service.MailService;
import com.server.domain.member.dto.MemberDto;
import com.server.domain.member.entity.Member;
import com.server.domain.member.mapper.MemberMapper;
import com.server.domain.member.service.MemberService;
import com.server.global.auth.jwt.JwtTokenizer;
import com.server.global.dto.SingleResponseDto;
import com.server.global.utils.UriCreator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {
    private static final String MEMBER_DEFAULT_URL = "/api/users";
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final MailService mailService;
    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/signup")
    public ResponseEntity<?> postMember(@Valid @RequestBody MemberDto.Post request) {
        Member createMember = memberService.createMember(memberMapper.memberDtoPostToMember(request));
        mailService.sendMail(createMember.getEmail(), "welcome");
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, createMember.getMemberId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<?> getMember(Principal principal) {
        Member member = memberService.findMemberByEmail(principal.getName());
        return new ResponseEntity<>(
            new SingleResponseDto<>(memberMapper.memberToMemberDtoResponse(member)), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> patchMember(@RequestBody MemberDto.Patch request, Principal principal) {
        Member updataMember = memberService.updateMember(principal.getName(),
            memberMapper.memberDtoPatchToMember(request));
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, updataMember.getMemberId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMember(HttpServletResponse response, Principal principal) {
        memberService.deleteMember(principal.getName());
        jwtTokenizer.resetHeaderRefreshToken(response);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password") //TODO: password 수정을 따로 분리한 이유? -> 비밀번호 재설정
    public ResponseEntity<?> patchPasswordMember(@RequestBody @Valid MemberDto.PasswordPatch request) {
        memberService.updatePassword(memberMapper.memberDtoPasswordPatchToMember(request));
        return ResponseEntity.ok().build();
    }

}
