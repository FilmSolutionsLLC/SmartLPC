package com.fps.config;

import java.util.Date;
import java.util.Properties;

import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SMTPSender {

	// TODO: Should ultimately retrieve these from a Properties file ...
	private static final String SMTP_LOGIN = "donotreply@filmsolutions.com";
	private static final String SMTP_PASS = "FSpass007";
	private static final String SMTP_SERVER = "10.0.1.11";
	private static final String SMTP_PORT = "25";

	public SMTPSender() {
	}

	public boolean send(String to, String cc, String bcc,
			String subject, String body, String[] attachments,
			String fromAppearanceString) {
		// NOTE: Addresses (to, cc, bcc) may be single addresses or
		// comma-separated lists of addresses
		try {
			Properties myProperties = new Properties();
			myProperties.put("mail.smtp.host", SMTP_SERVER);
			myProperties.put("mail.smtp.port", SMTP_PORT);
			myProperties.put("mail.smtp.auth", "true");
			myProperties.put("mail.smtp.timeout", "20000");
			myProperties.put("mail.smtp.connectiontimeout", "20000");
			myProperties.put("mail.smtp.notify", SMTP_LOGIN);
			myProperties.put("mail.smtp.sendpartial", true);
			myProperties.put("mail.debug", "false");

			// -- Attaching to default Session, or we could start a new one --
			Session session = Session.getDefaultInstance(myProperties, null);

			// -- Create a new message --
			Message msg = new MimeMessage(session);

			// -- Set the FROM field --
			InternetAddress fromAddress = InternetAddress.parse(
					fromAppearanceString, true)[0]; // Only a single "FROM"
													// address is allowed
			msg.setFrom(fromAddress);

			// -- Set TO, CC & BCC recipients --
			InternetAddress[] toAddresses = InternetAddress.parse(to, true);
			msg.setRecipients(Message.RecipientType.TO, toAddresses);

			if (cc != null)
				msg.addRecipients(Message.RecipientType.CC,
						InternetAddress.parse(cc, false));
			if (bcc != null)
				msg.addRecipients(Message.RecipientType.BCC,
						InternetAddress.parse(bcc, false));

			// -- Set the subject and body text --

			Multipart mp = new MimeMultipart();
			MimeBodyPart
			mbp = new MimeBodyPart();

			mbp = new MimeBodyPart();
			mbp.setContent(body, "text/plain");
			mp.addBodyPart(mbp);

			if (attachments != null) {
				// Create and fill the first message part
				for (int i = 0; i < attachments.length; i++) {
					if (attachments[i] != null) {
						File fileName;
						fileName = new File(attachments[i]);
						if (!fileName.exists()) {
							String fileExt = attachments[i].substring(0,
									attachments[i].length() - 4);
							System.out.println(fileExt);
							if (fileExt.equals(".pdf"))
								fileName = new File(attachments[i]);
							else
								fileName = new File(attachments[i].substring(0,
										attachments[i].length() - 4) + ".JPG");
						}
						mbp = new MimeBodyPart();
						FileDataSource fds = new FileDataSource(fileName);
						mbp.setDataHandler(new DataHandler(fds));
						mbp.setFileName(fds.getName());
						mp.addBodyPart(mbp);
					}
				}
			}
			msg.setSubject(subject);
			msg.setText(body);

			// -- Set some other header information --
			if (attachments != null) {
				msg.setContent(mp);
			} else {
				msg.setContent(body, "text/plain");
			}
			msg.setHeader("X-Mailer", "FilmPhotoSolutions");
			msg.setSentDate(new Date());
			msg.saveChanges();

			// -- Connect to the Transport
			Transport t = session.getTransport("smtp");
			t.connect(SMTP_SERVER, SMTP_LOGIN, SMTP_PASS);

			if (t.isConnected()) {
				// -- Send the message --

				t.sendMessage(msg, msg.getAllRecipients());
				t.close();
				System.out.println("Message sent OK.");
			} else {
				System.out
						.println("Message transmission failure due to host timeout");
				return false;
			}

			return true;
		} catch (Exception ex) {
			System.out.println("Message transmission failed");
			ex.printStackTrace();
			return false;
		}
	}
}
