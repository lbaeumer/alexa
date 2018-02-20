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

import de.alexa.ws.custom.KollegenIntent;
import de.alexa.ws.custom.PupsieIntent;
import de.alexa.ws.custom.beer.BierIntent;
import de.alexa.ws.custom.certificate.HSBCZertifikateIntent;
import de.alexa.ws.custom.devops.DevopsIntent;
import de.alexa.ws.news.DailyTradingNewsIntent;
import de.alexa.ws.news.HSBCNewsIntent;

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

		if (uri.matches("^/rs/alexa/news/dailytrading$")) {

			AlexaNewsIntent i = new DailyTradingNewsIntent();
			json = i.handleNewsIntent();
		} else if (uri.matches("^/rs/alexa/news/top$")) {

			AlexaNewsIntent i = new HSBCNewsIntent();
			json = i.handleNewsIntent();
		}

		resp.setContentType("application/json; charset=utf-8");
		resp.getWriter().write(gson.toJson(json));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uri = req.getRequestURI();
		log.info("uri=" + uri);

		if (uri.matches("^/rs/alexa/[a-z]+$")) {
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

			AlexaCustomResponse response = handleAlexaRequest(uri, request);

			String res = gson.toJson(response);
			log.info("response=" + res);

			resp.setContentType("application/json; charset=utf-8");
			resp.getWriter().write(res);
		}
	}

	private AlexaCustomResponse handleAlexaRequest(String uri, AlexaCustomRequest request) {

		AlexaCustomIntent intent = null;
		if (uri.matches("^/rs/alexa/bier$")) {
			intent = new BierIntent();
		} else if (uri.matches("^/rs/alexa/pupsie$")) {
			intent = new PupsieIntent();
		} else if (uri.matches("^/rs/alexa/hsbckollegen$")) {
			intent = new KollegenIntent();
		} else if (uri.matches("^/rs/alexa/hsbczertifikate$")) {
			intent = HSBCZertifikateIntent.getInstance();
		} else if (uri.matches("^/rs/alexa/devops$")) {
			intent = new DevopsIntent();
		}

		AlexaCustomResponse response = null;
		if ("LaunchRequest".equals(request.request.type)) {
			response = intent.handleLaunchIntent(request);
		} else if ("IntentRequest".equals(request.request.type)) {
			response = intent.handleIntent(request);
		} else if ("SessionEndedRequest".equals(request.request.type)) {
			//
			response = new AlexaCustomResponse("hoppla, session ended");
			log.warn("session ended");
		} else {
			log.warn("unknown type" + request.request.type);
		}

		return response;
	}
}
