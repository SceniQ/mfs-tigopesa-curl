/**

 * 

 */

package com.mfs.client.tigopesa.jar.util;

import org.apache.axis2.java.security.TrustAllTrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.*;
/**
 * 
 * @author Nihilent Technologies Pvt ltd.
 * 
 * 
 * 
 */
public class MTInterfaceServiceConnectorImpl {

	private static final Logger LOGGER = Logger.getLogger(MTInterfaceServiceConnectorImpl.class);
	private static final int CHECK_RESPONSE_CODE = 200;
	private final Properties mtinterfaceconnProperties = new Properties();
	private static final String CONNECTION_TIMEOUT = "60";
	private static final String CURL_HEADER_NAME = "--header";

	/**
	 *
	 * @param connectionData
	 *
	 * @param mtInterfaceConRequest
	 *
	 * @return String
	 *
	 * @throws UnknownHostException
	 *
	 * @throws IOException
	 *
	 * @throws ProtocolException
	 *
	 * @throws Exception
	 *
	 */

	private String connectionUsingHTTPS(String connectionData,

										final MTInterfaceConRequest mtInterfaceConRequest)

			throws Exception {

		if (LOGGER.isInfoEnabled()) {

			LOGGER.info("Start of connectionUsingHTTPS() in MTInterfaceServiceConnectorImpl");

		}

		SSLContext sslContext = null;

		try {

			sslContext = SSLContext.getInstance("SSL");

			sslContext

					.init(null,

							new javax.net.ssl.TrustManager[] { new TrustAllTrustManager() },

							new java.security.SecureRandom());

		} catch (Exception e) {

			LOGGER.info("Exception occured while setting SSL factory"+ e);

		}

		URL httpsUrl = new URL(mtInterfaceConRequest.getServiceUrl());

		sslContext.getSocketFactory().createSocket(httpsUrl.getHost(),

				mtInterfaceConRequest.getPort());

		HttpsURLConnection httpsConnection = (HttpsURLConnection) httpsUrl

				.openConnection();

		if (LOGGER.isInfoEnabled()) {

			LOGGER.info("Setting SSL Socket Factory...");

		}

		httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());

		httpsConnection.setRequestMethod(mtInterfaceConRequest

				.getHttpmethodName());

		// Send post request

		httpsConnection.setDoOutput(true);

		httpsConnection.setDoInput(true);

		setHeaderPropertiesToHTTPSConnection(mtInterfaceConRequest,

				connectionData, httpsConnection);

		if (connectionData != null) {

			DataOutputStream wr = new DataOutputStream(

					httpsConnection.getOutputStream());

			wr.writeBytes(connectionData);

			wr.flush();

			wr.close();

		}

		int responseCode = httpsConnection.getResponseCode();

		if (LOGGER.isInfoEnabled()) {

			LOGGER.info("Response Status from Adapter-->" + responseCode);

		}

		BufferedReader httpsResponse = null;

		if (responseCode == CHECK_RESPONSE_CODE) {

			httpsResponse = new BufferedReader(new InputStreamReader(

					httpsConnection.getInputStream()));

		} else {

			httpsResponse = new BufferedReader(new InputStreamReader(

					httpsConnection.getErrorStream()));

		}

		String output = readConnectionDataWithBuffer(httpsResponse);

		if (httpsResponse != null) {

			httpsResponse.close();

		}

		if (LOGGER.isInfoEnabled()) {

			LOGGER.info("Response from Adapter-->" + output);

			LOGGER.info("End of connectionUsingHTTPS() in MTInterfaceServiceConnectorImpl");

		}

		return output;

	}

	/**
	 * 
	 * @param MTInterfaceConRequest
	 * 
	 * @param data
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 * 
	 */

	public String connectByUsingCURL(String data,

			final MTInterfaceConRequest MTInterfaceConRequest)

			throws IOException {



		String responseData = null;

		Process p = null;

		BufferedReader sslResponse = null;

		InputStreamReader bis = null;

		InputStream is = null;

		try {

			String connectURL = MTInterfaceConRequest.getServiceUrl();

			String headersData = setHeaderPropertiesToCURLConnection(

					MTInterfaceConRequest, data);

				LOGGER.info("Adapter connecting relative URL :: " + connectURL);

				LOGGER.info("curl" + "-X " + MTInterfaceConRequest.getHttpmethodName() + "--insecure " + headersData
						+ "--data " + data + " " + connectURL);


			ProcessBuilder pb = new ProcessBuilder("curl", "-X", MTInterfaceConRequest.getHttpmethodName(),
					"--insecure", headersData,"--data", data,"--max-time",CONNECTION_TIMEOUT, connectURL);

			LOGGER.info("==> Printing Process Builder: " + pb.command());

			p = pb.start();

				LOGGER.info("Pb :" + pb.command());



			is = p.getInputStream();

			bis = new InputStreamReader(is);

			sslResponse = new BufferedReader(bis);

			responseData = readConnectionDataWithBuffer(sslResponse);

		} finally {

			if (p != null) {

				p.destroy();

			}

			if (sslResponse != null) {

				sslResponse.close();

			}

			if (bis != null) {

				bis.close();

			}

			if (is != null) {

				is.close();

			}

		}

		return responseData;

	}

	/**
	 * 
	 * @param httpsResponse
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 * 
	 */

	private String readConnectionDataWithBuffer(BufferedReader httpsResponse)

			throws IOException {


		String output;

		String responseLine;

		StringBuffer response = new StringBuffer();

		while ((responseLine = httpsResponse.readLine()) != null) {

			response.append(responseLine);

		}

		output = response.toString();

		return output;

	}

	/**
	 * 
	 * @param MTInterfaceConRequest
	 * 
	 * @param connectionData
	 * 
	 * @param httpsConnection
	 * 
	 */

	private void setHeaderPropertiesToHTTPSConnection(

			final MTInterfaceConRequest MTInterfaceConRequest,

			String connectionData, HttpsURLConnection httpsConnection) {


		httpsConnection.setRequestProperty("Connection", "Close");

		httpsConnection.setRequestProperty("Accept", "*/*");

		setHeadersToHTTPSConnection(MTInterfaceConRequest.getHeaders(),

				httpsConnection);

		if (connectionData != null && connectionData.length() > 0) {

			httpsConnection.setRequestProperty("Content-Length",

					String.valueOf(connectionData.length()));

		}

		if (LOGGER.isInfoEnabled()) {

			LOGGER.info("End of setHeaderPropertiesToHTTPSConnection() in MTInterfaceServiceConnectorImpl");

		}

	}

	private void setHeadersToHTTPSConnection(Map<String, String> headers,

			HttpsURLConnection httpsConnection) {

		if (headers != null && !headers.isEmpty() && httpsConnection != null) {

			for (Entry<String, String> entry : headers.entrySet()) {

				httpsConnection.setRequestProperty(entry.getKey(),

						entry.getValue());

			}

		}


	}

	/**
	 * 
	 * @param MTInterfaceConRequest
	 * 
	 * @param connectionData
	 * 
	 * @return String
	 * 
	 */

	private String setHeaderPropertiesToCURLConnection(

			final MTInterfaceConRequest MTInterfaceConRequest,

			String connectionData) {


		String curlHeaderProperties = setHeadersToCURLConnection(MTInterfaceConRequest

				.getHeaders());

		/*
		 * curlHeaderProperties = curlHeaderProperties + "," + CURL_HEADER_NAME + "," +
		 * "Connection: Close";
		 * 
		 * curlHeaderProperties = curlHeaderProperties + "," + CURL_HEADER_NAME + "," +
		 * "Accept:
		 * 
		 * 
		 * 
		 * if (connectionData != null && connectionData.length() > 0) {
		 * 
		 * curlHeaderProperties = curlHeaderProperties + ","
		 * 
		 * + CURL_HEADER_NAME + "," + "Content-Length:"
		 * 
		 * + connectionData.length();
		 * 
		 * }
		 * 
		 */

		setHeadersToCURLConnection(MTInterfaceConRequest.getHeaders());


		return curlHeaderProperties;

	}

	/**
	 * 
	 * @param headers
	 * 
	 * @return
	 * 
	 */

	private String setHeadersToCURLConnection(Map<String, String> headers) {


		String curlHeaderProperties = null;

		if (headers != null && !headers.isEmpty()) {

			int count = 0;

			for (Entry<String, String> entry : headers.entrySet()) {

				if (count == 0) {

					curlHeaderProperties = " " + CURL_HEADER_NAME + ",";

					curlHeaderProperties = curlHeaderProperties

							+ entry.getKey() + ":" + entry.getValue();

				} else {

					curlHeaderProperties = curlHeaderProperties + ","

							+ CURL_HEADER_NAME + "," + entry.getKey() + ":"

							+ entry.getValue();

				}

				count++;

			}

		}


		return curlHeaderProperties;

	}

}
