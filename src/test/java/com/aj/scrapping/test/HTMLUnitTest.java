package com.aj.scrapping.test;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.aj.scraping.util.Utils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HTMLUnitTest {

    private static final Log LOG = LogFactory.getLog(HTMLUnitTest.class);

    WebClient webClient = null;
    String url = null;

    @Before
    public void init() {
        url = Utils.getMessage("test.url");
        Assert.assertFalse(Utils.isEmpty(url));

        webClient = new WebClient(BrowserVersion.CHROME);
        Assert.assertNotNull(webClient);
    }

    @SuppressWarnings("unused")
    @Test
    public void parseInfo() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        // Enable CSS to modify classes
        webClient.getOptions().setCssEnabled(true);

        // Getting page to parse
        final Page page = webClient.getPage(url);

        // Checking if HTML
        Assert.assertTrue(page.isHtmlPage());

        // Getting HTMLPage
        final HtmlPage htmlPage = (HtmlPage) page;

        // XPATH POWER!!!! :'(
        HtmlDivision divByXPath = (HtmlDivision) htmlPage.getByXPath(
                "//*[@id='block-system-main']/div/div[1]/div/ul/li[1]/div[2]/h2/a/div/div").get(0);

        // Getting by ID
        boolean caseSensitive = false;
        HtmlDivision divByid = (HtmlDivision) htmlPage.getElementById("block-block-7", caseSensitive);

        // Getting by Class¿? No way, use XPath
        HtmlDivision divByClass = (HtmlDivision) htmlPage.getByXPath("/div[class='title']").get(0);

        Utils.logInfo(LOG, "ENDING HTMLUnit test");
    }
}
