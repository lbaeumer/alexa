package de.alexa.ws;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.alexa.ws.custom.DevopsIntent;
import de.alexa.ws.custom.PupsieIntent;
import de.alexa.ws.news.NewsIntent;

public class DispatcherEndpoint extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger
			.getLogger(DispatcherEndpoint.class.getName());

	private Gson gson = new GsonBuilder().setPrettyPrinting()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ").create();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uri = req.getRequestURI();
		Object json = null;

		if (uri.matches("^/rs/alexa/news$")) {

			AlexaNewsIntent i = new NewsIntent();
			json = i.handleNewsIntent();
		}

		resp.setContentType("application/json");
		resp.getWriter().write(gson.toJson(json));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uri = req.getRequestURI();
		Object json = null;

		log.info("uri=" + uri);
		if (uri.matches("^/rs/alexa/devops$")) {

			StringBuffer strb = new StringBuffer();
			BufferedReader reader = new BufferedReader(req.getReader());
			String line;
			while ((line = reader.readLine()) != null) {
				strb.append(line + "\n");
			}
			log.info("alexarequest=\n\n" + strb + "\n\n");
			Gson gson = new Gson();
			AlexaCustomRequest request = gson.fromJson(strb.toString(),
					AlexaCustomRequest.class);

			AlexaCustomIntent intent = null;

			if (uri.matches("^/rs/alexa/pupsie$")) {
				intent = new PupsieIntent();
			}
			if (uri.matches("^/rs/alexa/devops$")) {
				intent = new DevopsIntent();
			}

			if ("LaunchRequest".equals(request.request.type)) {
				json = intent.handleLaunchIntent(request);
			} else if ("IntentRequest".equals(request.request.type)) {
				json = intent.handleIntent(request);
			} else if ("SessionEndedRequest".equals(request.request.type)) {
				//
				json = new AlexaCustomResponse("hoppla, session ended");
				log.warn("session ended");
			} else {
				log.warn("unknown type" + request.request.type);
			}

			log.info("return" + gson.toJson(json));

			resp.setContentType("application/json");
			resp.getWriter().write(gson.toJson(json));
		}

		resp.setContentType("application/json");
		resp.getWriter().write(gson.toJson(json));
	}

}
