package com.zuehlke.reuters.mahout;

import java.io.Serializable;

public class ReutersMessage implements Serializable{
	private static final long serialVersionUID = 1729034073736715522L;
	private String id;
	private String topic;
	private String body;
	
	
	public ReutersMessage(String messageId, String topic,
			String body) {
				this.id = messageId;
				this.topic = topic;
				this.body = body;
	}

	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String message) {
		this.body = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
