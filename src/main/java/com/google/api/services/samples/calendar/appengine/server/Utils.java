/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.calendar.appengine.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.prediction.Prediction;
//import com.google.api.services.calendar.CalendarScopes;
//import com.google.api.services.calendar.CalendarScopes;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Utility class for JDO persistence, OAuth flow helpers, and others.
 *
 * @author Yaniv Inbar
 */
class Utils {

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single globally shared instance across your application.
	 */
	private static final AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory
			.getDefaultInstance();

	private static final Logger log = Logger
			.getLogger(CalendarAppEngineSample.class.getName());

	/** Global instance of the HTTP transport. */
	static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();

	/** Global instance of the JSON factory. */
	static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static GoogleClientSecrets clientSecrets = null;

	static GoogleClientSecrets getClientCredential() throws IOException {
		log.info("getClientCredential");
		if (clientSecrets == null) {
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
					new InputStreamReader(Utils.class
							.getResourceAsStream("/client_secrets.json")));
			log.info("clientSecrets=" + clientSecrets.toString());

			Preconditions.checkArgument(
					!clientSecrets.getDetails().getClientId()
							.startsWith("Enter ")
							&& !clientSecrets.getDetails().getClientSecret()
									.startsWith("Enter "),
					"Download client_secrets.json file from https://code.google.com/apis/console/"
							+ "?api=calendar into calendar-appengine-sample/src/main/resources/client_secrets.json");
		}
		return clientSecrets;
	}

	static String getRedirectUri(HttpServletRequest req) {
		log.info("getRedirectUri");
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		log.info("getRedirectUri " + url);
		url.setRawPath("/oauth2callback");
		return url.build();
	}

	static GoogleAuthorizationCodeFlow newFlow() throws IOException {
		// https://developers.google.com/identity/protocols/googlescopes
		log.info("newFlow");
		List<String> scopes = new ArrayList<String>();
		scopes.add("https://www.googleapis.com/auth/prediction");
		scopes.add("https://www.googleapis.com/auth/drive");
		scopes.add("https://www.googleapis.com/auth/cloud-platform");
		scopes.add("https://www.googleapis.com/auth/cloud-vision");
		scopes.add("https://www.googleapis.com/auth/cloud.useraccounts");
		scopes.add("https://ml.googleapis.com/v1/projects/luitest123/models/census/versions/v1:predict");
		scopes.add("https://www.googleapis.com/auth/devstorage.full_control");

		GoogleClientSecrets gcs = getClientCredential();
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, gcs, scopes)
						.setDataStoreFactory(DATA_STORE_FACTORY)
						.setAccessType("offline").build();

		log.info("flow=" + flow.getAccessType() + ";"
				+ flow.getAuthorizationServerEncodedUrl());
		return flow;
	}

	static Credential getCredential() throws IOException {
		log.info("getCredential");
		String userId = UserServiceFactory.getUserService().getCurrentUser()
				.getUserId();
		String email = UserServiceFactory.getUserService().getCurrentUser()
				.getEmail();
		log.info("getCredential; user=" + userId + ";" + email);
		Credential credential = newFlow().loadCredential(userId);
		return credential;
	}

	static Prediction loadPredictionClient() throws IOException {
		log.info("loadCalendarCLient");
		String userId = UserServiceFactory.getUserService().getCurrentUser()
				.getUserId();
		log.info("loadPredictionClient" + userId);
		Credential credential = newFlow().loadCredential(userId);
		return new Prediction.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName("luitest123")
				.build();
	}

	/**
	 * Returns an {@link IOException} (but not a subclass) in order to work
	 * around restrictive GWT serialization policy.
	 */
	static IOException wrappedIOException(IOException e) {
		if (e.getClass() == IOException.class) {
			return e;
		}
		return new IOException(e.getMessage());
	}

	private Utils() {
	}
}
