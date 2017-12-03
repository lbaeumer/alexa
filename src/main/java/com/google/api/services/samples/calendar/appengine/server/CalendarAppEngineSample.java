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

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Google Calendar Data API App Engine sample.
 * 
 * @author Yaniv Inbar
 */
public class CalendarAppEngineSample
		extends
			AbstractAppEngineAuthorizationCodeServlet {

	static final String APP_NAME = "Google Calendar Data API Sample Web Client";
	private static final Logger log = Logger

			.getLogger(CalendarAppEngineSample.class.getName());

	static final String GWT_MODULE_NAME = "calendar";

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("doGet");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.println("<!doctype html><html><head>");
		writer.println(
				"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		writer.println("<title>" + APP_NAME + "</title>");
		writer.println("<link type=\"text/css\" rel=\"stylesheet\" href=\""
				+ GWT_MODULE_NAME + ".css\">");
		writer.println(
				"<script type=\"text/javascript\" language=\"javascript\" "
						+ "src=\"" + GWT_MODULE_NAME + "/" + GWT_MODULE_NAME
						+ ".nocache.js\"></script>");
		writer.println("</head><body>");
		UserService userService = UserServiceFactory.getUserService();
		writer.println("<div class=\"header\"><b>"
				+ request.getUserPrincipal().getName() + "</b> | "
				+ "<a href=\""
				+ userService
						.createLogoutURL(request.getRequestURL().toString())
				+ "\">Log out</a> | "
				+ "<a href=\"http://code.google.com/p/google-api-java-client/source/browse"
				+ "/calendar-appengine-sample?repo=samples\">See source code for "
				+ "this sample</a></div>");
		writer.println("<div id=\"main\"/>");
		writer.println("</body></html>");
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req)
			throws ServletException, IOException {
		String url = Utils.getRedirectUri(req);
		log.info("getRedirectUri " + url);
		return url;
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		log.log(Level.INFO, "initializeFlow", new RuntimeException());
		return Utils.newFlow();
	}
}
