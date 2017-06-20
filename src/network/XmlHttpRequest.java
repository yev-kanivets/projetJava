package network;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import util.Out;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Util class that encapsulates HTTP request.
 * Created on 3/21/17.
 *
 * @author Evgenii Kanivets
 */
public class XmlHttpRequest {

    private static final int CONNECT_TIMEOUT = 10000; // 10 seconds
    private static final int READ_TIMEOUT = 10000; // 10 seconds

    private String stringUrl;

    public XmlHttpRequest(String stringUrl) {
        this.stringUrl = stringUrl;
    }

    public Document execute() {
        Document document = null;

        try {
            URL url = new URL(stringUrl);
            URLConnection yc = url.openConnection();
            yc.setConnectTimeout(CONNECT_TIMEOUT);
            yc.setReadTimeout(READ_TIMEOUT);
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputLine;

            String strXML = "";
            while ((inputLine = in.readLine()) != null) {
                strXML += inputLine;
            }
            in.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream inputStream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            document = builder.parse(new InputSource(inputStream));
            inputStream.close();
        } catch (Exception e) {
            Out.get().trace(e);
        }

        return document;
    }

}
