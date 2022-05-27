package com.soohyun.springSecurity;

import com.soohyun.springSecurity.model.Member;
import com.soohyun.springSecurity.model.Request.RequestLoginUser;
import com.soohyun.springSecurity.service.AuthService;
import com.soohyun.springSecurity.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    Member member;

    @BeforeEach()
    public void initMember(){
        this.member = new Member();
        this.member.setUsername("test5");
        this.member.setPassword("test5");
        this.member.setName("김동근");
        this.member.setEmail("ehdrms2034@naver.com");
        this.member.setAddress("대한민국 어디광역시 땡땡로 땡땡길 101동 1001호");
    }

    @Test
    public void signUp() {
        authService.signUpUser(member);
    }

    @Test
    public void login() {
        RequestLoginUser loginUser = new RequestLoginUser(member.getUsername(), member.getPassword());
        try {
            authService.loginUser(loginUser.getUsername(), loginUser.getPassword());
            log.info("로그인 성공");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void modifyUserRole(){

    }

    @Test
    public void sendFirstEmail(){
        emailService.sendMail("ehdrms2034@naver.com","테스트메일입니다.","ㅇㅇㅇ");
    }


}