package de.lennard.ws;

public interface AlexaCustomIntent {

	AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request);

	AlexaCustomResponse handleIntent(AlexaCustomRequest request);
}
