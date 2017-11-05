package de.alexa.ws.custom.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.alexa.dto.AllBuildsDTO;
import de.alexa.dto.AllJobsDTO;

public class JenkinsUtil {

	private static final Logger log = Logger
			.getLogger(JenkinsUtil.class.getName());

	private static final String HOSTNAME = "http://185.185.27.242:8080/jenkins";
	private static final String CRED = "webhook:xyz";

	public int startBuild(String name, String urlParameters)
			throws IOException {

		URL obj = new URL(HOSTNAME + "/job/" + name
				+ "/buildWithParameters?token=1234567890");

		log.info("start " + obj);
		log.info("params " + urlParameters);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "FancyDevOps");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		String pw = DatatypeConverter.printBase64Binary(CRED.getBytes());
		// String pw = DatatypeConverter
		// .printBase64Binary("admin:hsbc".getBytes());
		con.setRequestProperty("Authorization", "Basic " + pw);

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		try {
			wr.writeBytes(urlParameters);
			wr.flush();
		} finally {
			wr.close();
		}

		int responseCode = con.getResponseCode();
		log.info("Response Code : " + responseCode);
		if (responseCode >= 400) {
			return responseCode;
		}

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		try {
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			// print result
			log.info(response.toString());

		} finally {
			in.close();
		}

		return responseCode;
	}

	public AllBuildsDTO getAllBuilds(String name) throws IOException {

		URL obj = new URL(HOSTNAME + "/job/" + name + "/api/json");

		log.info("get all builds " + obj);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "FancyDevOps");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		String pw = DatatypeConverter.printBase64Binary(CRED.getBytes());
		// String pw = DatatypeConverter
		// .printBase64Binary("admin:hsbc".getBytes());
		con.setRequestProperty("Authorization", "Basic " + pw);

		int responseCode = con.getResponseCode();
		log.info("Response Code : " + responseCode);
		if (responseCode >= 400) {
			return null;
		}

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		try {
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			// print result
			log.info(response.toString());

			Gson gson = new Gson();
			AllBuildsDTO b = gson.fromJson(response.toString(),
					AllBuildsDTO.class);

			log.info("b=" + b);

			return b;
		} finally {
			in.close();
		}
	}

	public AllJobsDTO getAllJobs() throws IOException {

		URL obj = new URL(HOSTNAME + "/api/json");

		log.info("get all jobs");

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "FancyDevOps");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		String pw = DatatypeConverter.printBase64Binary(CRED.getBytes());
		// String pw = DatatypeConverter
		// .printBase64Binary("admin:hsbc".getBytes());
		con.setRequestProperty("Authorization", "Basic " + pw);

		int responseCode = con.getResponseCode();
		log.info("Response Code : " + responseCode);
		if (responseCode >= 400) {
			return null;
		}

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		try {
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			// print result
			log.info(response.toString());

			Gson gson = new Gson();
			AllJobsDTO b = gson.fromJson(response.toString(), AllJobsDTO.class);

			log.info("b=" + b);

			return b;
		} finally {
			in.close();
		}
	}
	
	public SyndFeed getAllJobsFromRSS() throws IOException, JAXBException, IllegalArgumentException, FeedException {
		URL obj = new URL(HOSTNAME + "/rssAll");

		log.info("get all builds " + obj);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "FancyDevOps");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		String pw = DatatypeConverter.printBase64Binary(CRED.getBytes());
		// String pw = DatatypeConverter
		// .printBase64Binary("admin:hsbc".getBytes());
		con.setRequestProperty("Authorization", "Basic " + pw);

		int responseCode = con.getResponseCode();
		log.info("Response Code : " + responseCode);
		if (responseCode >= 400) {
			return null;
		}

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		try {

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(in);
			
			return feed;
		} finally {
			in.close();
		}

	}
}
