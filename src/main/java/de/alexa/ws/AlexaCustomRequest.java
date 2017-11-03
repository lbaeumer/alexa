package de.alexa.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AlexaCustomRequest {
	public String version;

	public RequestSession session;
	public Request request;

	public static class Request {
		public String requestId, locale, type;
		public Date timestamp;
		public RequestIntent intent;
	}

	public static class RequestIntent {
		public String name;
		public Map<String, Map<String, Object>> slots = new HashMap<String, Map<String, Object>>();
	}

	public static class RequestSession {
		public String sessionId;
	}
}
