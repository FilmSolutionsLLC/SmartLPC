package com.fps.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.SMTPSender;
import com.fps.domain.Lookups;
import com.fps.domain.ProjectRoles;
import com.fps.repository.LookupsRepository;
import com.fps.web.rest.dto.MailDTO;

@RestController
@RequestMapping("/api")
public class MailResource {
	private final Logger log = LoggerFactory.getLogger(MailResource.class);

	@Inject
	private JdbcTemplate jdbcTemplate;

	@Inject
	private LookupsRepository lookupsRepository;

	@Inject
	private SMTPSender smtpSender;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mailer/talent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public MailDTO getMailTalentValues() {

		MailDTO mailDTO = new MailDTO();

		List<Lookups> mailAttributes = lookupsRepository.findByTableName("mail_talent");

		for (Lookups lookups : mailAttributes) {
			
			if (lookups.getFieldName().equals("from")) {
				mailDTO.setFrom(lookups.getTextValue());
			} else if (lookups.getFieldName().equals("subject")) {
				mailDTO.setSubject(lookups.getTextValue());
			} else {
				mailDTO.setBody(lookups.getTextValue());
			}
		}
		return mailDTO;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mailer/execs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public MailDTO getMailExecsValues() {

		MailDTO mailDTO = new MailDTO();

		List<Lookups> mailAttributes = lookupsRepository.findByTableName("mail_execs");

		for (Lookups lookups : mailAttributes) {
			
			if (lookups.getFieldName().equals("from")) {
				mailDTO.setFrom(lookups.getTextValue());
			} else if (lookups.getFieldName().equals("subject")) {
				mailDTO.setSubject(lookups.getTextValue());
			} else {
				mailDTO.setBody(lookups.getTextValue());
			}
		}
		return mailDTO;
	}
	
	@RequestMapping(value = "/mailer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public MailDTO postMailValues(@RequestBody MailDTO mailDTO) throws URISyntaxException {
		
		
		for(String to: mailDTO.getTo()){
		
			smtpSender.send("rohan@filmsolutions.com", "", "", mailDTO.getSubject().concat(" for ").concat(to), mailDTO.getBody(), null, "donotreply@filmsolutions.com");
		}
		return mailDTO;
	}
	
	
	
}
