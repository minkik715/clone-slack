package com.webproject.chatservice.utils;

import com.webproject.chatservice.models.User;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
//메일을 보낼 클래스
public class MailUtil {
   // @Value("${spring.mail.host}")
    private String hostSMTP = "";

    //@Value("${spring.mail.username}")
    private String hostSMTPid = "";

   // @Value("${spring.mail.password}")
    private String hostSMTPpw = "";

    public int sendMail(User user) throws Exception {
        Random r = new Random();
        int dice = r.nextInt(157211)+48271;

        //Mail Server 설정
        String charSet = "utf-8";
        //보내는 사람
        String fromEmail = this.hostSMTPid + "@naver.com";
        String fromName = "gaemangtalk";

        String title = "비밀번호 찾기 인증 이메일 입니다.";    //제목
        String content =

                System.getProperty("line.separator")+

                        System.getProperty("line.separator")+

                        "안녕하세요 회원님 저희 홈페이지를 찾아주셔서 감사합니다"

                        +System.getProperty("line.separator")+

                        System.getProperty("line.separator")+

                        "비밀번호 찾기 인증번호는 " +dice+ " 입니다. "

                        +System.getProperty("line.separator")+

                        System.getProperty("line.separator")+

                        "받으신 인증번호를 홈페이지에 입력해 주시면 다음으로 넘어갑니다."; // 내용

        //email 전송
        String mailRecipient = user.getEmail(); //받는 사람 이메일 주소
        try {
            //객체 선언
            HtmlEmail mail = new HtmlEmail();
            mail.setDebug(true);
            mail.setCharset(charSet);
            mail.setSSLOnConnect(true); //SSL 사용
            mail.setHostName(hostSMTP);
            mail.setSmtpPort(465); //SMTP 포트 번호
            mail.setAuthentication(hostSMTPid, hostSMTPpw);
            mail.setStartTLSEnabled(true); //TLS 사용
            mail.addTo(mailRecipient, charSet);
            mail.setFrom(fromEmail, fromName, charSet);
            mail.setSubject(title);
            mail.setHtmlMsg(content);
            mail.send();

            return dice;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}