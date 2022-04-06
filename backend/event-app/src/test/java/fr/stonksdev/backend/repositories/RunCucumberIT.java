package fr.stonksdev.backend.repositories;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("fr/stonksdev/backend/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fr.stonksdev.backend.features")
public class RunCucumberIT {
}
