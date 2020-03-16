package io.github.samuelkinnett.hocr4j.dom;

import io.github.samuelkinnett.hocr4j.dom.HocrParser;
import io.github.samuelkinnett.hocr4j.dom.HocrTag;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * HocrTag Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Oct 4, 2013</pre>
 */
public class HocrElementTest {


    HocrTag element = (HocrTag) HocrParser.createAst("<body><p>Hello</p> <div><b>w</b>orld</div></body>").get(0);

    /**
     * Method: getRawText()
     */
    @Test
    public void testGetRawText() throws Exception {
        assertEquals("Hello world", element.getRawText());
    }

    /**
     * Method: findTag(String tagName)
     */
    @Test
    public void testFindTag() throws Exception {
        assertEquals("Hello", element.findTag("p").getRawText());
        assertEquals("w", element.findTag("b").getRawText());
        assertEquals("world", element.findTag("div").getRawText());
        assertNull(element.findTag("em"));
        assertTrue(element == element.findTag("body"));
    }

    /**
     * Method: isBlank()
     */
    @Test
    public void testIsBlank() throws Exception {
        assertFalse(element.isBlank());
        assertFalse(element.getElements().get(0).isBlank());
        assertTrue(element.getElements().get(1).isBlank());
        assertFalse(element.getElements().get(2).isBlank());
    }

} 
