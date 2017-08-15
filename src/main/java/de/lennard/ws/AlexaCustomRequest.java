package de.lennard.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AlexaCustomRequest {
	public FuRequest1 request;

	public static class FuRequest1 {
		public String requestId, locale, type;
		public Date timestamp;
		public FuIntent intent;
	}

	public static class FuIntent {
		public String name;
		public Map<String, Map<String, Object>> slots = new HashMap<String, Map<String, Object>>();
	}
}
