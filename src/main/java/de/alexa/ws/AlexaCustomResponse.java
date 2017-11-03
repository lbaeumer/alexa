package de.alexa.ws;

import java.util.HashMap;
import java.util.Map;

public class AlexaCustomResponse {
	public String version = "1.0";
	public Response response;

	public AlexaCustomResponse() {
		response = new Response();
	}
	public AlexaCustomResponse(String text) {
		response = new Response(text);
	}
	public AlexaCustomResponse(String title, String content) {
		response = new Response(title + " " + content, title, content);
	}

	public void setRepromt(String text) {
		response.reprompt = new HashMap<String, Map<String, String>>();
		HashMap<String, String> m = new HashMap<String, String>();
		response.reprompt.put("outputSpeech", m);
		m.put("type", "PlainText");
		m.put("text", text);
		shouldEndSession(false);

		response.outputSpeech.put("text",
				response.outputSpeech.get("text") + " " + text);
	}

	public void shouldEndSession(boolean b) {
		response.shouldEndSession = b;
	}

	public static class Response {
		public Response() {
		}
		public Response(String text) {
			outputSpeech.put("type", "PlainText");
			outputSpeech.put("text", text);
		}
		public Response(String text, String title, String content) {
			this(text);
			card = new HashMap<String, Object>();
			card.put("type", "Simple");
			card.put("title", title);
			card.put("content", content);
		}

		public Response(String text, String title, String content,
				String imageUrl) {
			this(text, title, content);
			Map<String, String> image = new HashMap<>();
			card.put(imageUrl, image);
			image.put("smallImageUrl", imageUrl);
		}

		public Map<String, String> outputSpeech = new HashMap<String, String>();
		public Map<String, Object> card;
		public Map<String, Map<String, String>> reprompt;
		public Boolean shouldEndSession;
	}

}
