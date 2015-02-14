package com.homlin.module.commom.mail;

public interface MailService {

	void send(String subject, String content, String sentto) throws Exception;

	void testEmailConfig(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail)
			throws Exception;

}
