package com.fps.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fps.domain.Message;
import com.fps.elastics.search.MessageRepository;
import com.fps.service.ScheduleTask;

@RestController
@RequestMapping("/api")
public class ChatResource {
	private final Logger log = LoggerFactory.getLogger(ChatResource.class);
	
	@Inject
	private ScheduleTask sheduleTask;
	
	@Inject
	private MessageRepository messageRepository;

	@RequestMapping(value = "/chat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void recieveChats(@RequestBody Message message) throws URISyntaxException {
		log.debug("REST request to save Captions : {}", message);
		message.setId(messageRepository.count());
		messageRepository.save(message);
		sheduleTask.trigger();

	}
	@RequestMapping(value = "/chat", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public Iterable<Message> allChats() throws URISyntaxException {
		Iterable<Message> allMessages = messageRepository.findAll();
		return allMessages;

	}
}
