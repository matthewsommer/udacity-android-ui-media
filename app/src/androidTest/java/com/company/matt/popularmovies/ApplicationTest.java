package com.company.matt.popularmovies;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public static Test suite() {
        return new TestSuiteBuilder(ApplicationTest.class)
                .includeAllPackagesUnderHere().build();
    }
    public ApplicationTest() {
        super(Application.class);
    }
}