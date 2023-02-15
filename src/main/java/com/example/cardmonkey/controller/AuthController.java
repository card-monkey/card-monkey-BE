package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.LoginRequest;
import com.example.cardmonkey.dto.LoginResponse;
import com.example.cardmonkey.dto.PasswordDTO;
import com.example.cardmonkey.dto.SignupRequest;
import com.example.cardmonkey.service.MemberService;
import com.example.cardmonkey.entity.Token;
import com.example.cardmonkey.repository.TokenRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"인증 서비스"}, description = "인증 서비스를 담당합니다.")
public class AuthController {

    private final MemberService memberService;
    private final TokenRepository tokenRepository;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입을 수행합니다.")
    public String signUp(@RequestBody SignupRequest req) {
        if (req.getUserId() == null || req.getPassword() == null || req.getName() == null) {
            return "모든 값을 입력해주세요";
        }
        return memberService.signup(req);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인을 수행합니다.")
    public LoginResponse signIn(@RequestBody LoginRequest req) {
        return memberService.login(req);
    }

    /* 로그인 예시코드 =========================
    @PostMapping("/sign-in")
    public String signIn(UserDTO loginUserDto) {
        String loginUserResponse = userService.signin(loginUserDto);
        return loginUserResponse;
    }
    ==================================== */


    /* 권한확인 예시코드 ==========================
    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('USER')") // USER 권한만 호출 가능
    public String hello(@AuthenticationPrincipal UserDTO user) {
        return user.getEmail() + ", 안녕하세요!";
    }
    ==================================== */

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃을 수행합니다.")
    public String logout(@RequestHeader(name="Authorization") String header) {
        tokenRepository.save(Token.builder().token(header).build());
        return "로그아웃 완료";
    }

    /**
     * 비밀번호 변경
     */
    @PostMapping("/changePassword/{id}")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호를 확인 후 변경합니다.")
    public String changePassword(@PathVariable("id") String id, @RequestBody PasswordDTO dto) {
        if (dto.getCurrentPassword() == null) {
            return "현재 비밀번호를 입력해주세요";}
        if (dto.getNewPassword() == null) {
            return "새로운 비밀번호를 입력해주세요";}
        if (memberService.updatePassword(id, dto.getCurrentPassword(), dto.getNewPassword()) == null) {
            return "현재 비밀번호가 일치하지않습니다.";}
        if (dto.getCurrentPassword().equals(dto.getNewPassword())) {
            return "현재 비밀번호와 동일합니다.";
        } else {
            return "비밀번호가 변경되었습니다.";
        }
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/deleteAccount/{id}")
    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴합니다.")
    public String deleteAccount(@PathVariable String id) {
        try {
            memberService.deleteAccount(id);
            return "회원탈퇴 완료";
        } catch (Exception e) {
            e.printStackTrace();
            return "회원탈퇴 실패";
        }
    }
}
