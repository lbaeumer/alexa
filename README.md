## setting a comment

JT-1 #comment this is evil hard to fix

JT-8 #comment why does this not work?

#comment JT-17 this bug is quite new      - this does not work

#comment JT-17 this bug is quite new3      - this does not work

JT-17 #comment this bug is quite new2     - this works

JT-17 #comment this bug is quite new4     - this works

## setting time - does not work

JT-5 #time 1w 2h this quite difficult
JT-5 #time 1w 2d 4h 30m Total work logged

## closing - does not work

JT-5 #close 1w 2d 4h 30m Total work logged

## closing - does not work

JT-17 #close this bug is quite new5 - does not work

JT-17 #done this bug is quite new6

JT-17 #in-progress this bug is quite new7

JT-17 #selected-for-development this bug is quite new8 #commment and a new comment

appengine-flexible-archetype
============================

This is a generated App Engine Flexible Java application from the appengine-flexible-archetype archetype.

## Requirements

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi) (at least 3.3.9)
* [Gradle](https://gradle.org/gradle-download/) (optional)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud)

Initialize the Google Cloud SDK using:

    gcloud init

This skeleton is ready to run.

## Maven

### Run Locally

    mvn jetty:run

### Deploy

    mvn appengine:deploy

### Test Only

    mvn verify

## Modify the Skeleton

As you add / modify the source code [(`src/main/java/...`)](src/main/java/) it's very useful to add unit testing
to [(`src/main/test/...`)](src/main/test/).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)

## Updating to latest Artifacts

An easy way to keep your projects up to date is to use the maven [Versions plugin][versions-plugin].

    mvn versions:display-plugin-updates
    mvn versions:display-dependency-updates
    mvn versions:use-latest-versions

Our usual process is to test, update the versions, then test again before committing back.

[plugin]: http://www.mojohaus.org/versions-maven-plugin/

