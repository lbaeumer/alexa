package de.alexa.ws.custom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;

public class DevopsIntent implements AlexaCustomIntent {

	private static final Logger log = Logger
			.getLogger(DevopsIntent.class.getName());

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json = null;

		log.info("going with " + request.request.intent.name);
		if ("DeployIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots;
				String name = slots.get("application").get("value").toString();

				String ret;
				if (name.equalsIgnoreCase("cobam")
						|| name.equalsIgnoreCase("test")) {
					ret = "I'm happy to deploy application " + name
							+ " for you.";
				} else {
					ret = "Sorry " + name + ", I don't know appliction " + name
							+ ". Let me build test for you.";
					name = "test";
				}

				URL url = new URL(
/*
						"http://capture.mobilesol.de:8080/jenkins/job/"
								+ name
								+ "/buildWithParameters?token=1234567890");
*/
					"http://35.198.237.60/job/"
					+ name
					+ "/buildWithParameters?token=1234567890");
						
				startBuild(url, "payload=hallo&repositoryUrl=http://myurl");

				json = new AlexaCustomResponse(ret);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse("Ich bin doof");
			}

		} else if ("BuildStatusIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots;
				String name = slots.get("application").get("value").toString();

				String ret;
				ret = "The Build of your application " + name + " is successful.";

				json = new AlexaCustomResponse(ret);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse("Ich bin doof");
			}

		}

		return json;
	}

	void startBuild(URL obj, String urlParameters) throws IOException {

		log.info("start " + obj);
		log.info("params " + urlParameters);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "FancyDevOps");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
//		String pw = DatatypeConverter
//				.printBase64Binary("webhook:xyz".getBytes());
		String pw = DatatypeConverter
				.printBase64Binary("admin:hsbc".getBytes());
		con.setRequestProperty("Authorization", "Basic " + pw);

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		log.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		log.info(response.toString());
	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"I'm happy to serve your DevOps requests.");
	}

}
