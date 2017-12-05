package com.epam.page.object.generator.integrationalTests;

import com.epam.page.object.generator.PageObjectsGenerator;
import com.epam.page.object.generator.adapter.JavaPoetAdapter;
import com.epam.page.object.generator.containers.SupportedTypesContainer;
import com.epam.page.object.generator.errors.LocatorTransformationException;
import com.epam.page.object.generator.parser.JsonRuleMapper;
import com.epam.page.object.generator.utils.LocatorTransformation;
import com.epam.page.object.generator.validators.ValidatorsStarter;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;

public class EpamJobListingTest {
    private String outputDir = "src/test/resources/";
    private String packageName = "test";

    private PageObjectsGenerator initPog(String jsonPath, List<String> urls,
                                         boolean checkLocatorUniqueness,
                                         boolean forceGenerateFiles) throws IOException {
        SupportedTypesContainer bc = new SupportedTypesContainer();

        JsonRuleMapper parser = new JsonRuleMapper(new File(jsonPath), new ObjectMapper());

        LocatorTransformation locatorTransformation = new LocatorTransformation();

        JavaPoetAdapter javaPoetAdapter = new JavaPoetAdapter(bc, locatorTransformation);

        ValidatorsStarter validatorsStarter = new ValidatorsStarter(bc);
        validatorsStarter.setCheckLocatorsUniqueness(checkLocatorUniqueness);

        PageObjectsGenerator pog = new PageObjectsGenerator(parser, validatorsStarter,
                javaPoetAdapter, outputDir, urls, packageName);

        pog.setForceGenerateFile(forceGenerateFiles);

        return pog;
    }

    @Before
    public void setUp() throws IOException {
        FileUtils.deleteDirectory(new File(outputDir + packageName));
    }


    @Test
    public void epamJobListeningTest()
            throws ParseException, IOException, URISyntaxException, LocatorTransformationException {
        List<String> urls = asList(
                "https://www.epam.com/careers/job-listings/job.11515#apply");

        initPog(
                "src/test/resources/epamjoblisting/rules.json",
                urls,
                false,
                true)
                .generate();
    }

}
