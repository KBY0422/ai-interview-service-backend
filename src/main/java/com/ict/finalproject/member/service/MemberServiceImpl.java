package com.ict.finalproject.member.service;

import com.ict.finalproject.member.mapper.MemberMapper;
import com.ict.finalproject.member.vo.MemberVO;
import com.ict.finalproject.member.vo.RefreshVO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    // application.yaml에 있는 SMTP 메일 발송 값들 가져오기
    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.username}")
    private String username;
    @Value("${mail.smtp.password}")
    private String password;


    @Override
    public boolean idCheck(String m_id) {
        return memberMapper.idCheck(m_id) > 0;
    }

    @Override
    public boolean sendCode(String m_email, HttpSession session) {
        try {
            String code = createCode();
            sendMail(m_email, code);

            session.setAttribute("authCode", code);
            session.setAttribute("authEmail", m_email);
            session.setAttribute("authTime", System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean sendPasswordResetCode(String m_email, HttpSession session) {
        try{
            String code = createCode();
            sendPasswordResetMail(m_email, code);

            session.setAttribute("ResetCode", code);
            session.setAttribute("authEmail", m_email);
            session.setAttribute("authTime", System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean newPassword(MemberVO member, String encodedNewPassword) {
        try {
            memberMapper.newPassword(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int updateMyInfo(MemberVO mvo) {
        return memberMapper.updateMyInfo(mvo);
    }

    @Override
    public void deactivateMember(int m_idx) {
        memberMapper.deactivateMember(m_idx);
    }


    @Override
    public int register(MemberVO mvo) {
        return memberMapper.register(mvo);
    }

    @Override
    public MemberVO findById(String m_id) {
        return memberMapper.findById(m_id);
    }

    @Override
    public MemberVO findByIdx(Integer mIdx) {
        return memberMapper.findByIdx(mIdx);
    }

    @Override
    public MemberVO findByEmail(String m_email) {return memberMapper.findByEmail(m_email);}

    @Override
    public void saveRefreshToken(String m_id, String refreshToken, Date expiration) {
        memberMapper.saveRefreshToken(new RefreshVO(m_id, refreshToken, expiration));
    }

    @Override
    public RefreshVO getRefreshToken(String m_id) {
        return memberMapper.getRefreshToken(m_id);
    }

    @Override
    public MemberVO findId(Map<String,String> map) {
        return memberMapper.findId(map);
    }

    // 메일 인증번호 생성
    private String createCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 메일 발송
    private void sendMail(String m_email, String code) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "AI-InterView 관리자"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(m_email));
            message.setSubject("[AI-InterView] 회원가입 이메일 인증번호 안내");
            message.setText(
                    "회원가입 인증번호 안내입니다.\n\n" +
                            "인증번호 : " + code + "\n\n" +
                            "5분 이내에 입력해 주세요."
            );
            Transport.send(message);
        } catch (UnsupportedEncodingException e){
            log.info("오류 내용 : {}", e);
        }
    }

    private void sendPasswordResetMail(String m_email, String code) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "AI-InterView 관리자"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(m_email));
            message.setSubject("[AI-InterView] 비밀번호 초기화 이메일 인증번호 안내");
            message.setText(
                    "비밀번호 초기화 인증번호 안내입니다.\n\n" +
                            "인증번호 : " + code + "\n\n" +
                            "5분 이내에 입력해 주세요."
            );
            Transport.send(message);
        } catch (UnsupportedEncodingException e){
            log.info("오류 내용 : {}", e);
        }
    }

    private void sendQuitMail(String m_email, String code) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "AI-InterView 관리자"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(m_email));
            message.setSubject("[AI-InterView] 회원 탈퇴 인증번호 안내");
            message.setText(
                    "회원 탈퇴를 위한 인증번호 안내입니다.\n\n" +
                            "인증번호 : " + code + "\n\n" +
                            "본인이 요청한 경우에만 입력해 주세요.\n" +
                            "5분 이내에 입력하지 않으면 자동으로 만료됩니다."
            );

            Transport.send(message);
        } catch (UnsupportedEncodingException e) {
            log.error("회원 탈퇴 메일 발송 중 오류", e);
        }
    }


    @Override
    public boolean sendQuitCode(String m_email, HttpSession session) {
        try {
            String code = createCode();
            sendQuitMail(m_email, code);

            session.setAttribute("authCode", code);
            session.setAttribute("authEmail", m_email);
            session.setAttribute("authTime", System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            log.error("회원 탈퇴 인증 메일 발송 실패", e);
            return false;
        }
    }

}
