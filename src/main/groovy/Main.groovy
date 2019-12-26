
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import groovy.util.XmlSlurper

class Main {


    private static final String GET = "GET";
    private static final String ACCESS_KEY_ID = "9b1bf64f8f139523076af120cb60c8a9";
    private static final String SECRET_ACCESS_KEY = "db9f26a3da9750d08e3308c4ad7521f4efccba54f06c518cd14bc76b96666903d3b021c5b37b8766a8bd4e017eae3f1862d3ce9f12eaa7caac0aa968c2480c25";

    /**
     * The main method for our demo.  This makes a simple API call using our example HMAC signing class
     * and writes the response to the output stream.
     *
     * @param args command line arguments - ignored
     */
    static void main(final String[] args) {
        try {

            def applicationsApiUrl = new URL("https://analysiscenter.veracode.com/api/5.0/getapplist.do");
            def authorizationHeader = HmacRequestSigner.getVeracodeAuthorizationHeader(ACCESS_KEY_ID, SECRET_ACCESS_KEY, applicationsApiUrl, "POST");

            println(authorizationHeader)

//            def connection = (HttpsURLConnection) applicationsApiUrl.openConnection();
//            connection.setRequestMethod(GET);
//
//            connection.setRequestProperty("Authorization", authorizationHeader);
//
//            println(authorizationHeader)
//            println(connection.getResponseCode())
//

//            def responseInputStream = connection.getInputStream()
//
//            def textBuilder = new StringBuilder();
//            def reader = new BufferedReader(new InputStreamReader(responseInputStream, Charset.forName(StandardCharsets.UTF_8.name())))
//
//            for (int c = 0;  c != -1; c = reader.read()) {
//                textBuilder.append((char) c);
//            }

//            def list = new XmlSlurper().parseText(textBuilder)


        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }


    private static void readResponse(InputStream responseInputStream) throws IOException, JSONException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] responseBytes = new byte[16384];
        int x = 0;
        while ((x = responseInputStream.read(responseBytes, 0, responseBytes.length)) != -1) {
            outputStream.write(responseBytes, 0, x);
        }
        outputStream.flush();
        System.out.println((new JSONObject(outputStream.toString())).toString(4));
    }

}