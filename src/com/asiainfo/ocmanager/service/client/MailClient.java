package com.asiainfo.ocmanager.service.client;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.utils.MailServerProperties;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Mail client.
 * 
 * @author Ethan
 *
 */
public class MailClient {
	private static final Logger LOG = LoggerFactory.getLogger(MailClient.class);
	private Properties props = new Properties();
	private Session session;
	private static final String ACCOUNT = "account";
	private static final String PASSWD = "password";
	private String account;
	private String passwd;
	private static MailClient instance;

	public static MailClient getInstance() {
		if (instance == null) {
			synchronized (MailClient.class) {
				if (instance == null) {
					instance = new MailClient();
				}
			}
		}
		return instance;
	}

	private MailClient() {
		props = MailServerProperties.getConf();
		account = props.getProperty(ACCOUNT);
		passwd = props.getProperty(PASSWD);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(account),
				"'account' parameter missing in mailserver.properties.");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(passwd),
				"'password' parameter missing in mailserver.properties.");
		session = Session.getInstance(props, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, passwd);
			}

		});
	}

	public void sendMsgs(String toAddress, String subject, String msgText) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Going to send mail to [{}] by subject [{}] of text [{}]", toAddress, subject, msgText);
			}
			Preconditions.checkNotNull(toAddress, "toAddress is null.");
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(account));
			InternetAddress[] address = { new InternetAddress(toAddress) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setContent(msgText, "text/html; charset=utf-8");
			Transport.send(msg);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Mail successfully sended to [{}] of text [{}]", toAddress, msgText);
			}
		} catch (Exception e) {
			LOG.error("Exception while send message: ", e);
			throw new RuntimeException("Exception while send message: ", e);
		}
	}

}
