package de.lennard.ws;

import java.util.HashMap;
import java.util.Map;

public class AlexaCustomResponse {
	public String version = "1.0";
	public FuResponse1 response;

	public AlexaCustomResponse() {
		response = new FuResponse1();
	}
	public AlexaCustomResponse(String text) {
		response = new FuResponse1(text);
	}
	public AlexaCustomResponse(String title, String content) {
		response = new FuResponse1(title + " " + content, title, content);
	}

	public void setRepromt(String text) {
		response.reprompt = new HashMap<String, Map<String, String>>();
		HashMap<String, String> m = new HashMap<String, String>();
		response.reprompt.put("outputSpeech", m);
		m.put("type", "PlainText");
		m.put("text", text);
	}
	
	public void shouldEndSession(boolean b) {
		response.shouldEndSession = b;
	}

	public static class FuResponse1 {
		public FuResponse1() {
		}
		public FuResponse1(String text) {
			outputSpeech.put("type", "PlainText");
			outputSpeech.put("text", text);
		}
		public FuResponse1(String text, String title, String content) {
			outputSpeech.put("type", "PlainText");
			outputSpeech.put("text", text);
			card = new HashMap<String, Object>();
			card.put("type", "Simple");
			card.put("title", title);
			card.put("content", content);
		}

		public Map<String, String> outputSpeech = new HashMap<String, String>();
		public Map<String, Object> card;
		public Map<String, Map<String, String>> reprompt;
		public Boolean shouldEndSession;
	}

}
