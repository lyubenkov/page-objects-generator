package com.epam.page.object.generator.integrationalTests;

import com.epam.page.object.generator.PageObjectsGenerator;
import com.epam.page.object.generator.adapter.JavaPoetAdapter;
import com.epam.page.object.generator.containers.SupportedTypesContainer;
import com.epam.page.object.generator.errors.NotUniqueSelectorsException;
import com.epam.page.object.generator.errors.ValidationException;
import com.epam.page.object.generator.model.SearchRule;
import com.epam.page.object.generator.parser.JsonRuleMapper;
import com.epam.page.object.generator.utils.XpathToCssTransformation;
import com.epam.page.object.generator.validators.ValidatorsStarter;
import com.epam.page.object.generator.validators.ValidationContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class MainTest {

    private String outputDir = "src/test/resources/";
    private String packageName = "test";

    @Before
    public void setUp() throws IOException {
        FileUtils.deleteDirectory(new File(outputDir + packageName));
    }

    private PageObjectsGenerator initPog(String jsonPath, String url,
                                         boolean checkLocatorUniqueness,
                                         boolean forceGenerateFiles) throws IOException {
        List<String> urls = new ArrayList<>();

        urls.add(url);

        SupportedTypesContainer bc = new SupportedTypesContainer();

        JsonRuleMapper parser = new JsonRuleMapper(new File(jsonPath), new ObjectMapper());

        XpathToCssTransformation xpathToCssTransformation = new XpathToCssTransformation();

        JavaPoetAdapter javaPoetAdapter = new JavaPoetAdapter(bc, xpathToCssTransformation);

        ValidatorsStarter validator = new ValidatorsStarter();

        validator.setCheckLocatorsUniqueness(checkLocatorUniqueness);

        PageObjectsGenerator pog = new PageObjectsGenerator(parser, validator, javaPoetAdapter, outputDir, urls, packageName);

        pog.setForceGenerateFile(forceGenerateFiles);

        return pog;
    }

    @Test
    public void pageObjectsGenerator_ok() throws Exception {

        PageObjectsGenerator pog = initPog(
            "src/test/resources/dropdown.json",
            "https://www.w3schools.com/howto/howto_js_dropdown.asp",
            true,
            false);

        pog.generatePageObjects();
    }

    @Test(expected = ValidationException.class)
    public void pageObjectsGenerator_wrongType() throws Exception {
        PageObjectsGenerator pog = initPog(
            "src/test/resources/dropdown-wrong-type.json",
            "https://www.w3schools.com/howto/howto_js_dropdown.asp",
            true,
            false);

        pog.generatePageObjects();
    }

    @Test(expected = ValidationException.class)
    public void pageObjectsGenerator_forceFileGenerate() throws Exception {
        PageObjectsGenerator pog = initPog(
            "src/test/resources/dropdown-wrong-type.json",
            "https://www.w3schools.com/howto/howto_js_dropdown.asp",
            true,
            true);

        pog.generatePageObjects();
    }

    @Test(expected = ValidationException.class)
    public void pageObjectsGenerator_NotUniqueLocatorFoundInUniquenessModeOn() throws Exception {
        PageObjectsGenerator pog = initPog(
            "src/test/resources/button.json",
            "https://www.google.com",
            true,
            false);

        pog.generatePageObjects();
    }

    @Test
    public void pageObjectsGenerator_NotUniqueLocatorFoundInUniquenessModeOff() throws Exception {
        PageObjectsGenerator pog = initPog(
            "src/test/resources/button.json",
            "https://www.google.com",
            false,
            false);

        pog.generatePageObjects();
    }

}