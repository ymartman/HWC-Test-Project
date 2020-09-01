# hello-world-challenge Test solution

## Story and Challenge Task

> **As a** user  
> **I want to** send Hello request  
> **So that** I am greeted by the app in response

## Fast guide:
> IDE require lombok plugin to be installed

### Package structure:
**main
com.libertex.qa.challenge.api - mvc like parts to describe and work with tested application
com.libertex.qa.challenge.config - part to provide an easy way to custom configure project
com.libertex.qa.challenge.logic - a shortcuts to use application functional
com.libertex.qa.core.reporting - minimum reporting sugar
com.libertex.qa.core.api - base to work with Rest services
**test
com.libertex.qa.hello - asked tests
com.libertex.qa.other - adds to test service interface