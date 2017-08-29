package com.fps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fps.domain.Message;
import com.fps.elastics.search.MessageRepository;

import java.util.Date;

import javax.inject.Inject;

@Service
public class ScheduleTask {

	@Autowired
	private SimpMessagingTemplate template;


	@Inject
	private MessageRepository messageRepository;

	// this will send a message to an endpoint on which a client can subscribe
	// @Scheduled(fixedRate = 500)
	public void trigger() {

		Message message = messageRepository.findOne(messageRepository.count() - 1);
		String chatMessage = message.getUser() + " : " + message.getMessage();
		this.template.convertAndSend("/topic/message", chatMessage);
	}

	// clearing chat from spring elastic search
	@Scheduled(cron = "0 0 0 * * *")
	public void clearChat() {

		messageRepository.deleteAll();
	}

}
