package com.mfs.client.tigopesa.jar;


import com.mfs.client.tigopesa.jar.util.MTInterfaceConRequest;
import com.mfs.client.tigopesa.jar.util.MTInterfaceServiceConnectorImpl;

import java.io.IOException;
import java.util.HashMap;
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

        processResponse(response);
    }

    /**
     * Processes the response coming from calling the partner
     */
    private static void processResponse(String response){

        System.out.println("Transaction detail Response: ");
        System.out.println(response);
    }
}
