package com.epam.page.object.generator.utils;

import com.epam.page.object.generator.errors.LocatorTransformationException;
import com.epam.page.object.generator.model.SearchRule;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class LocatorTransformation {

//    private Invocable getInvocableJs(String pathToJsFile, String jsFunctionName, String exceptionMessage, String locator)
//            throws LocatorTransformationException {
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//
//        try {
//            engine.eval(new FileReader(pathToJsFile));
//
//            Invocable invocable = (Invocable) engine;
//
//            return (SearchRule) invocable.invokeFunction(jsFunctionName, locator);
//        } catch (NoSuchMethodException | ScriptException | FileNotFoundException ex) {
//            throw new LocatorTransformationException(
//                    "Failed to transform Xpath to Css locator in this search rule:"
//                            + searchRule);
//        }
//    }

    public void xpathToCssTransformRule(SearchRule searchRule) throws LocatorTransformationException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            engine.eval(new FileReader("src/main/resources/cssify.js"));

            Invocable invocable = (Invocable) engine;

            searchRule.setCss(invocable.invokeFunction("cssify", searchRule.getXpath()).toString());
            searchRule.setXpath(null);
        } catch (NoSuchMethodException | ScriptException | FileNotFoundException ex) {
            throw new LocatorTransformationException(
                "Failed to transform Xpath to Css locator in this search rule:"
                    + searchRule);
        }
    }

    public String cssToXpathTransformRule(String cssLocator) throws LocatorTransformationException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            engine.eval(new FileReader("src/main/resources/css2xpath.js"));
            Invocable invocable = (Invocable) engine;

            return invocable.invokeFunction("css2xpath", cssLocator).toString();
        } catch (NoSuchMethodException | ScriptException | FileNotFoundException ex) {
            throw new LocatorTransformationException(
                    "Failed to transform Css to Xpath locator:"
                            + cssLocator);
        }
    }

}