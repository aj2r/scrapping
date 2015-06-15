package com.aj.scrapping.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.aj.scraping.exception.HTTPNotFoundException;
import com.aj.scraping.util.Utils;
import com.aj.scraping.util.html.HTMLUtils;
import com.aj.scraping.util.html.JsoupUtils;

/** See <a href="http://jsoup.org/apidocs/">Jsoup apidoc</a> for more information.
 * @author ajrodriguez
 *
 */
public class JsoupTest {

    private static final Log LOG = LogFactory.getLog(JsoupTest.class);

    Document jsoupDoc = null;

    /** Load Jsoup Document from {@link HTMLUtils} 
     * @throws HTTPNotFoundException
     */
    @Before
    public void loadJsoup() throws HTTPNotFoundException {

        final String url = Utils.getMessage("test.url");
        Assert.assertFalse(Utils.isEmpty(url));

        jsoupDoc = JsoupUtils.getJsoupDocument(url, null);
        Assert.assertNotNull(jsoupDoc);

    }

    /**
     * Different ways to get information
     */
    @SuppressWarnings("unused")
    @Test
    public void parseInfo() {

        // Each method throw a new Exception
        Validate.notNull(jsoupDoc);

        // Using Collector. Internally uses Visitor to extract elements.

        final Elements collectByTag = Collector.collect(new Evaluator.Tag("section"), jsoupDoc);

        // ¿? Make no sense: Elements but getting by Id
        final Elements collectById = Collector.collect(new Evaluator.Id("block-block-7"), jsoupDoc);

        // Getting element by Id
        final Element elementById = jsoupDoc.getElementById("block-block-7");

        // Getting element by tag
        final Elements elementsByTag = jsoupDoc.getElementsByTag("section");

        // Getting element by class
        final Elements elementsByClass = jsoupDoc.getElementsByClass("title");

        /** Getting element by regex. Internally uses Selector <a href="http://tool.oschina.net/uploads/apidocs/jsoup-1.6.3/org/jsoup/select/Selector.html"> 
         * and QueryParser
         * Remember:  {@link Document} is a {@link Element}
         */
        final Elements select = jsoupDoc.select(":matchesOwn(Presentes en la Drupal Summer Academy)");

        /**
         * Modify an Element
         */
        Validate.notNull(elementsByClass);
        final Element element = elementsByClass.get(0);

        // Adding a class

        element.addClass("testing-class"); // to Element

        elementsByClass.addClass("testing-class"); // to Elements

        // It's necessary?
        jsoupDoc.getElementsByClass("title").addClass("testing-class");

        Utils.logInfo(LOG, "ENDING Jsoup test");

        // USE SELENIUM DRIVER TO MODIFY ORIGINAL HTML TO SCRAPPED HTML

    }

}
