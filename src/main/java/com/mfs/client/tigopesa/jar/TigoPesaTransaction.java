package com.mfs.client.tigopesa.jar;


import com.mfs.client.tigopesa.jar.util.MTInterfaceConRequest;
import com.mfs.client.tigopesa.jar.util.MTInterfaceServiceConnectorImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.*;

import static com.mfs.client.tigopesa.jar.util.TigoPesaServiceConstants.CONNECTION;
import static com.mfs.client.tigopesa.jar.util.TigoPesaServiceConstants.CONTENT_TYPE;

public class TigoPesaTransaction {

    private static final Logger LOGGER = Logger.getLogger(MTInterfaceServiceConnectorImpl.class);
    private static final MTInterfaceServiceConnectorImpl mTInterfaceService = new MTInterfaceServiceConnectorImpl();

    public static void main(String[] args) {
        try {
            getTransactionDetails(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getTransactionDetails(String[] args) throws IOException {

        String mfsReferenceId = args[0];
        String type = args[1];
        String msisdn = args[2];
        String pin = args[3];
        String externalRef = args[4];
        String url = args[5];

        String request = "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\n"
                + "<COMMAND>\n"
                + "	    <TYPE>" + type + "</TYPE>\n"
                + "	    <REFERENCEID>" + mfsReferenceId + "</REFERENCEID>\n"
                + "		<MSISDN>" + msisdn + "</MSISDN>\n"
                + "		<PIN>" + pin + "</PIN>\n"
                + "		<EXTERNALREFID>" + externalRef + "</EXTERNALREFID>\n"
                + "</COMMAND>";

        LOGGER.info("==> Transaction Details Service - Prepared Request : " + request);

        MTInterfaceConRequest conRequest = new MTInterfaceConRequest();
        conRequest.setHttpmethodName("POST");
        conRequest.setServiceUrl(url);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type",CONTENT_TYPE);
        headers.put("Connection",CONNECTION);

        conRequest.setHeaders(headers);
        LOGGER.info("==> Transaction Details Service - Connection Request : " + conRequest);

        String response = mTInterfaceService.connectByUsingCURL(request,conRequest);

        processResponse(response, mfsReferenceId);
    }

    /**
     * Processes the response coming from calling the partner
     */
    private static void processResponse(String response,String mfsReferenceId){
        StringBuilder builder = new StringBuilder();
        int resultCode;

        if(response != null && response.contains("<RESULTCODE>")){
            resultCode = Integer.parseInt(response.substring(response.indexOf("<RESULTCODE>"),response.indexOf("</RESULTCODE>")).replace("<RESULTCODE>","").trim());
            if(0 == resultCode)
                builder.append("01, ").append(mfsReferenceId).append(", SUCCESS");
            else if(tigoPesaErrorCodes().contains(resultCode))
                builder.append("100, ").append(mfsReferenceId).append(", FAIL");
            else if(-1 == resultCode | 100 == resultCode | 901 == resultCode)
                builder.append("268, ").append(mfsReferenceId).append(", PENDING");
        }else{
            builder.append("268, ").append(mfsReferenceId).append(", PENDING");
        }

        System.out.println("Transaction detail Response: ");
        System.out.println(builder);
    }

    /**
     * TIGO PESA error codes
     *
     * @return list of transaction error codes
     */
    private static List<Integer> tigoPesaErrorCodes() {
        List<Integer> codes = new ArrayList<Integer>();
//        codes.add(-1);
//        codes.add(100);
//        codes.add(901);
        codes.add(100002);
        codes.add(100005);
        codes.add(100099);
        codes.add(100110);
        codes.add(100116);
        codes.add(100117);
        codes.add(100119);
        codes.add(100120);
        codes.add(100124);
        codes.add(100125);
        codes.add(100126);
        codes.add(100128);
        codes.add(100141);
       return codes;
    }
}
