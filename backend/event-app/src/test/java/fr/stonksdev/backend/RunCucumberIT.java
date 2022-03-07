package fr.stonksdev.backend;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("fr/stonksdev/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fr.stonksdev.features")
public class RunCucumberIT {
}
