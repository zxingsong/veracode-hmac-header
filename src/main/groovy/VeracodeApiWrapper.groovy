import groovy.util.slurpersupport.GPathResult

import javax.swing.text.Document
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
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

class VeracodeApiWrapper {


    private static final String GET = "GET";
    private static final String POST = "POST";
    private String veracodeId = "9b1bf64f8f139523076af120cb60c8a9";
    private String veracodeKey = "db9f26a3da9750d08e3308c4ad7521f4efccba54f06c518cd14bc76b96666903d3b021c5b37b8766a8bd4e017eae3f1862d3ce9f12eaa7caac0aa968c2480c25";

    VeracodeApiWrapper(String veracodeId, String veracodeKey) {
       this.veracodeId = veracodeId
       this.veracodeKey = veracodeKey
    }


    private GPathResult readResponseAndParseXml(HttpsURLConnection connection) {
        def textBuilder = new StringBuilder()
        def reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))

        for (int c = 0; c != -1; c = reader.read()) {
            textBuilder.append((char) c)
        }

        return new XmlSlurper().parseText(textBuilder.toString().trim())
    }

    void findApplicationId(String applicationName) {
        def applicationsApiUrl = new URL("https://analysiscenter.veracode.com/api/5.0/getapplist.do");
        def authorizationHeader = HmacRequestSigner.getVeracodeAuthorizationHeader(this.veracodeId, this.veracodeKey, applicationsApiUrl, GET);

        try {
            def connection = (HttpsURLConnection) applicationsApiUrl.openConnection();
            connection.setRequestMethod(GET);

            connection.setRequestProperty("Authorization", authorizationHeader)

            def applications = this.readResponseAndParseXml(connection)

            def result = applications.'*'.find { node -> node.@app_name == applicationName }

            if (!result) {
                println('not found')
            } else (
                println(result.@app_id)
            )

        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    void findBuildsRunningOnApplication(int applicationId) {
        def applicationsApiUrl = new URL("https://analysiscenter.veracode.com/api/5.0/getbuildinfo.do");
        def authorizationHeader = HmacRequestSigner.getVeracodeAuthorizationHeader(this.veracodeId, this.veracodeKey, applicationsApiUrl, POST);

        println(authorizationHeader)

//        try {
//            Map<String,Object> params = new LinkedHashMap<>();
//            params.put("app_id", applicationId);
//
//            StringBuilder postData = new StringBuilder();
//            for (Map.Entry<String,Object> param : params.entrySet()) {
//                if (postData.length() != 0) postData.append('&');
//                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//                postData.append('=');
//                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//            }
//            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
//
//            def connection = (HttpsURLConnection) applicationsApiUrl.openConnection();
//            connection.setRequestMethod(POST);
//            connection.setRequestProperty("Authorization", authorizationHeader)
//
//            connection.setDoOutput(true);
//            connection.getOutputStream().write(postDataBytes);
//
//
//            def response = this.readResponseAndParseXml(connection)
//
//            if (response.children()) {
//                println('scans running')
//            } else {
//                println('no scans')
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace()
//        }
    }

    void deleteBuilds(int applicationId) {
        def applicationsApiUrl = new URL("https://analysiscenter.veracode.com/api/5.0/deletebuild.do");
        def authorizationHeader = HmacRequestSigner.getVeracodeAuthorizationHeader(this.veracodeId, this.veracodeKey, applicationsApiUrl, POST);

        try {
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("app_id", applicationId);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");


            def connection = (HttpsURLConnection) applicationsApiUrl.openConnection();
            connection.setRequestMethod(POST);
            connection.setRequestProperty("Authorization", authorizationHeader)


//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);


            def response = this.readResponseAndParseXml(connection)


            def result = applications.'*'.find { node -> node.@app_name == applicationName }

            if (!result) {
                println('not found')
            } else (
                    println(result.@app_id)
            )

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}