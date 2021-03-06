package com.soohyun.springSecurity.service.impl;

import com.soohyun.springSecurity.config.UserRole;
import com.soohyun.springSecurity.model.Member;
import com.soohyun.springSecurity.model.Request.RequestSocialData;
import com.soohyun.springSecurity.model.Salt;
import com.soohyun.springSecurity.model.SocialData;
import com.soohyun.springSecurity.repository.MemberRepository;
import com.soohyun.springSecurity.repository.SocialDataRepository;
import com.soohyun.springSecurity.service.AuthService;
import com.soohyun.springSecurity.service.EmailService;
import com.soohyun.springSecurity.service.RedisUtil;
import com.soohyun.springSecurity.service.SaltUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SaltUtil saltUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SocialDataRepository socialDataRepository;

    @Override
    @Transactional
    public void signUpUser(Member member) {
        String password = member.getPassword();
        String salt = saltUtil.genSalt();
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt,password));
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void signUpSocialUser(RequestSocialData member){
        Member newMember = new Member();
        newMember.setUsername(member.getId());
        newMember.setPassword("");
        newMember.setEmail(member.getEmail());
        newMember.setName(member.getName());
        newMember.setAddress("");
        newMember.setSocial(new SocialData(member.getId(),member.getEmail(),member.getType()));
        memberRepository.save(newMember);
    }

    @Override
    public Member loginSocialUser(String id, String type) throws NotFoundException{
        SocialData socialData = socialDataRepository.findByIdAndType(id,type);
        if(socialData==null) throw new NotFoundException("????????? ???????????? ??????");
        return socialData.getMember();
    }

    @Override
    public Member loginUser(String id, String password) throws Exception{
        Member member = memberRepository.findByUsername(id);
        if(member==null) throw new Exception ("????????? ???????????? ??????");
        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt,password);
        if(!member.getPassword().equals(password))
            throw new Exception ("??????????????? ????????????.");
        if(member.getSocial()!=null)
            throw new Exception ("?????? ???????????? ????????? ????????????.");
        return member;
    }

    @Override
    public Member findByUsername(String username) throws NotFoundException {
        Member member = memberRepository.findByUsername(username);
        if(member == null) throw new NotFoundException("????????? ???????????? ??????");
        return member;
    }

    @Override
    public void verifyEmail(String key) throws NotFoundException {
        String memberId = redisUtil.getData(key);
        Member member = memberRepository.findByUsername(memberId);
        if(member==null) throw new NotFoundException("????????? ??????????????????");
        modifyUserRole(member,UserRole.ROLE_USER);
        redisUtil.deleteData(key);
    }

    @Override
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/user/verify/";
        if(member==null) throw new NotFoundException("????????? ???????????? ??????");
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(),member.getUsername(), 60 * 30L);
        emailService.sendMail(member.getEmail(),"[????????? ?????????] ???????????? ?????????????????????.",VERIFICATION_LINK+uuid.toString());
    }

    @Override
    public void modifyUserRole(Member member, UserRole userRole){
        member.setRole(userRole);
        memberRepository.save(member);
    }

    @Override
    public boolean isPasswordUuidValidate(String key){
        String memberId = redisUtil.getData(key);
        return !memberId.equals("");
    }

    @Override
    public void changePassword(Member member,String password) throws NotFoundException{
        if(member == null) throw new NotFoundException("changePassword(),????????? ???????????? ??????");
        String salt = saltUtil.genSalt();
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt,password));
        memberRepository.save(member);
    }


    @Override
    public void requestChangePassword(Member member) throws NotFoundException{
        String CHANGE_PASSWORD_LINK = "http://localhost:8080/user/password/";
        if(member == null) throw new NotFoundException("????????? ???????????? ??????.");
        String key = REDIS_CHANGE_PASSWORD_PREFIX+UUID.randomUUID();
        redisUtil.setDataExpire(key,member.getUsername(),60 * 30L);
        emailService.sendMail(member.getEmail(),"[????????? ?????????] ????????? ???????????? ?????? ??????",CHANGE_PASSWORD_LINK+key);
    }


}