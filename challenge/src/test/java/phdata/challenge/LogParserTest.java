/*
 */
package phdata.challenge;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class LogParserTest {

    private String sampleMsgs = "200.4.93.122 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; {1C69E7AA-C14E-200E-5A77-8EAB2D667A07})\"\n"
            + "155.156.150.253 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Opera/9.00 (Windows NT 5.1; U; en)\"\n"
            + "155.157.99.22 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/5.0 (Windows; U; Windows NT 5.0; ja-JP; rv:1.7.12) Gecko/20050919 Firefox/1.0.7\"\n"
            + "155.156.103.181 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Opera/9.00 (Windows NT 5.1; U; en)\"\n"
            + "155.156.140.104 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.8.1.8) Gecko/20071004 Iceweasel/2.0.0.8 (Debian-2.0.0.6+2.0.0.8-Oetch1)\"";

    private final String[] sample;

    public LogParserTest() {
        sample = sampleMsgs.split("\\r?\\n");
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getText method, of class LogParser.
     */
    @Test
    public void testGetText() {
        System.out.println("getText");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getText();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIp method, of class LogParser.
     */
    @Test
    public void testGetIp() {
        System.out.println("getIp");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getIp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTimestamp method, of class LogParser.
     */
    @Test
    public void testGetTimestamp() {
        System.out.println("getTimestamp");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getTimestamp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBytecount method, of class LogParser.
     */
    @Test
    public void testGetBytecount() {
        System.out.println("getBytecount");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getBytecount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRequest method, of class LogParser.
     */
    @Test
    public void testGetRequest() {
        System.out.println("getRequest");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getRequest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResponse method, of class LogParser.
     */
    @Test
    public void testGetResponse() {
        System.out.println("getResponse");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getResponse();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReferer method, of class LogParser.
     */
    @Test
    public void testGetReferer() {
        System.out.println("getReferer");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getReferer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBrowser method, of class LogParser.
     */
    @Test
    public void testGetBrowser() {
        System.out.println("getBrowser");
        LogParser instance = null;
        String expResult = "";
        String result = instance.getBrowser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class LogParser.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        LogParser instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
