package de.alexa.ws;

public interface AlexaCustomIntent {

	AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request);

	AlexaCustomResponse handleIntent(AlexaCustomRequest request);
}
