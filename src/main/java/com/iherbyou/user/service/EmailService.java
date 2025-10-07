package com.iherbyou.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * 이메일 인증 메일 발송
     */
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail); // iherbyou 측 email
            helper.setTo(toEmail); // 회원가입 한 사람의 email (받는 사람 이메일 설정)
            helper.setSubject("[iHerbYou] [verification email] 이메일 인증을 완료해주세요");

            // 인증 링크 생성
            String verificationUrl = baseUrl + "/api/users/verify-email?token=" + token;

            // HTML 이메일 본문
            String htmlContent = buildVerificationEmailHtml(verificationUrl);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("이메일 인증 메일 발송 완료: {}", toEmail);

        } catch (MessagingException e) {
            log.error("이메일 발송 실패: {}", toEmail, e);
            throw new RuntimeException("이메일 발송에 실패했습니다", e);
        }
    }

    /**
     * 비밀번호 재설정 메일 발송
     */
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("[iHerbYou] 비밀번호 재설정 안내");

            // 재설정 링크 생성
            String resetUrl = baseUrl + "/api/users/reset-password-confirm?token=" + token;

            String htmlContent = buildPasswordResetEmailHtml(resetUrl);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("비밀번호 재설정 메일 발송 완료: {}", toEmail);

        } catch (MessagingException e) {
            log.error("비밀번호 재설정 메일 발송 실패: {}", toEmail, e);
            throw new RuntimeException("이메일 발송에 실패했습니다", e);
        }
    }

    /**
     * 이메일 인증 메일 HTML 생성
     */
    private String buildVerificationEmailHtml(String verificationUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                </head>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                        <h2 style="color: #4CAF50;">아이허브유 회원가입을 환영합니다!</h2>
                        <p>안녕하세요,</p>
                        <p>아이허브유에 가입해 주셔서 감사합니다.</p>
                        <p>아래 버튼을 클릭하여 이메일 인증을 완료해주세요.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" 
                               style="display: inline-block; padding: 12px 24px; background-color: #4CAF50; 
                                      color: white; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                이메일 인증하기
                            </a>
                        </div>
                        <p style="color: #666; font-size: 14px;">
                            링크가 작동하지 않는 경우, 아래 URL을 복사하여 브라우저에 붙여넣으세요:<br>
                            <a href="%s" style="color: #4CAF50;">%s</a>
                        </p>
                        <p style="color: #999; font-size: 12px; margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px;">
                            본 메일은 발신 전용입니다. 문의사항은 고객센터를 이용해주세요.<br>
                            © 2025 아이허브유. All rights reserved.
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(verificationUrl, verificationUrl, verificationUrl);
    }

    /**
     * 비밀번호 재설정 메일 HTML 생성
     */
    private String buildPasswordResetEmailHtml(String resetUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                </head>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                        <h2 style="color: #4CAF50;">비밀번호 재설정 안내</h2>
                        <p>안녕하세요,</p>
                        <p>비밀번호 재설정 요청을 받았습니다.</p>
                        <p>아래 버튼을 클릭하여 새로운 비밀번호를 설정해주세요.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" 
                               style="display: inline-block; padding: 12px 24px; background-color: #4CAF50; 
                                      color: white; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                비밀번호 재설정하기
                            </a>
                        </div>
                        <p style="color: #666; font-size: 14px;">
                            링크가 작동하지 않는 경우, 아래 URL을 복사하여 브라우저에 붙여넣으세요:<br>
                            <a href="%s" style="color: #4CAF50;">%s</a>
                        </p>
                        <p style="color: #ff6b6b; font-size: 14px; font-weight: bold;">
                            이 링크는 1시간 동안만 유효합니다.
                        </p>
                        <p style="color: #666; font-size: 14px;">
                            본인이 요청하지 않았다면 이 메일을 무시하셔도 됩니다.<br>
                            비밀번호는 변경되지 않습니다.
                        </p>
                        <p style="color: #999; font-size: 12px; margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px;">
                            본 메일은 발신 전용입니다. 문의사항은 고객센터를 이용해주세요.<br>
                            © 2025 아이허브유. All rights reserved.
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(resetUrl, resetUrl, resetUrl);
    }

}
