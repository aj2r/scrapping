package com.aj.scrapping.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.aj.scraping.exception.HTTPNotFoundException;
import com.aj.scraping.util.Utils;
import com.aj.scraping.util.html.HTMLUtils;

public class HtmlParserTest {

    private static final Log LOG = LogFactory.getLog(HtmlParserTest.class);

    Parser parser = null;

    @Before
    public void init() throws HTTPNotFoundException {
        final String url = Utils.getMessage("test.url");
        Assert.assertFalse(Utils.isEmpty(url));

        final String html = HTMLUtils.checkAndRetrieveHTML(url, null);
        parser = Parser.createParser(html, Utils.DEFAULT_CHARSET.name());
        Assert.assertNotNull(parser);
    }

    @Test
    public void parseInfo() throws ParserException {

        MyPersonalVisitor visitor = new MyPersonalVisitor();

        NodeList nodes = parser.parse(null);
        final NodeList divs = nodes.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"),
                new HasAttributeFilter("class", "item-list")), true);
        Assert.assertNotNull(divs);
        final Node node = divs.elementAt(0);
        node.accept(visitor);
    }

    private static class MyPersonalVisitor extends NodeVisitor {

        private MiData currentData, lastData;
        private Tag lastTag;

        private enum MiData {
            TITLE;
        }

        public void visitTag(final Tag tag) {
            final String tagName = tag.getTagName();
            final String classStr = tag.getAttribute("class");

            if ("title".equals(classStr)) {
                currentData = MiData.TITLE;
            }

            lastData = currentData;

            lastTag = tag;
            super.visitTag(tag);
        }

        public void visitStringNode(final Text string) {
            if (currentData != null) {
                switch (currentData) {
                case TITLE:
                    Utils.logInfo(LOG, string.getText());
                }
            }

            super.visitStringNode(string);
        }

        @Override
        public void visitEndTag(final Tag tag) {
            currentData = null;
            super.visitEndTag(tag);
        }
    }
}
