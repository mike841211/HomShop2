package com.homlin.module.commom.mail;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.util.CacheUtil;

@Service
public class MailServiceImpl implements MailService {

	@Override
	public void send(String subject, String content, String sentto) throws Exception {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(CacheUtil.getConfig(CacheConfigKeys.EMAIL_HOST));
		javaMailSenderImpl.setUsername(CacheUtil.getConfig(CacheConfigKeys.EMAIL_USERNAME));
		javaMailSenderImpl.setPassword(CacheUtil.getConfig(CacheConfigKeys.EMAIL_PASSWORD));
		MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
		mimeMessageHelper.setFrom(CacheUtil.getConfig(CacheConfigKeys.EMAIL_ADDRESS));
		mimeMessageHelper.setTo(sentto);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(content, true);
		javaMailSenderImpl.send(mimeMessage);
	}

	@Override
	public void testEmailConfig(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword,
			String toMail) throws Exception {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(smtpHost);
		javaMailSenderImpl.setPort(smtpPort);
		javaMailSenderImpl.setUsername(smtpUsername);
		javaMailSenderImpl.setPassword(smtpPassword);
		MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
		mimeMessageHelper.setFrom(smtpFromMail);
		mimeMessageHelper.setTo(toMail);
		mimeMessageHelper.setSubject("邮箱发送配置测试邮件：成功！");
		mimeMessageHelper.setText("邮箱发送配置测试邮件：成功！", true);
		javaMailSenderImpl.send(mimeMessage);
	}

}
