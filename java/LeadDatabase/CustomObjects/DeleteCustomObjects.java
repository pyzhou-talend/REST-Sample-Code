/*
   DeleteCustomObjects.java

   Marketo REST API Sample Code
   Copyright (C) 2016 Marketo, Inc.

   This software may be modified and distributed under the terms
   of the MIT license.  See the LICENSE file for details.
*/
package dev.marketo.samples.CustomObjects;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

//the Java sample code on dev.marketo.com uses the minimal-json package
//minimal-json provides easy and fast representations of JSON
//for more information check out https://github.com/ralfstx/minimal-json

public class DeleteCustomObjects {
	//You should only use this example on a sandbox environment
	//It will REALLY delete objects permanently
	public String marketoInstance = ;//Replace this with the host from Admin Web Services
	public String marketoIdURL = marketoInstance + "/identity";	
	public String clientId = ;	//Obtain from your Custom Service in Admin>Launchpoint
	public String clientSecret = ;	//Obtain from your Custom Service in Admin>Launchpoint
	public String idEndpoint = marketoIdURL + "/oauth/token?grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
	public int[] ids; //one or more IDs to delete
	public String dedupeBy; //string to specify marketoIds or dedupeFields
	public String[] externalIds; //one or more external IDs to delete
	public String name;//name of custom object type
	
	public static void main(String[] args){
		DeleteCustomObjects deleteCustoms = new DeleteCustomObjects();
		deleteCustoms.dedupeBy = "idField";
		deleteCustoms.ids = new int[]{10000,20000,30000};
		deleteCustoms.name = "pet";
		String result = deleteCustoms.postData();
		System.out.println(result);
	}
	public String postData(){
		String result = null;
		try {
			//Assemble the URL
			String endpoint = marketoInstance + "/rest/v1/customobjects/" + name + "/delete.json?access_token=" + getToken();
			JsonObject requestBody = buildRequest();
			URL url = new URL(endpoint.toString());
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("Content-type", "application/json");//"application/json" content-type is required.
            urlConn.setRequestProperty("accept", "text/json");
            urlConn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
			wr.write(requestBody.toString());
			wr.flush();
			int responseCode = urlConn.getResponseCode();
			if (responseCode == 200){
				InputStream inStream = urlConn.getInputStream();
				result = convertStreamToString(inStream);
			}else{
				result = "Status Code: " + responseCode;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	public JsonObject buildRequest(){
		JsonObject requestBody = new JsonObject();
		JsonArray inputArray = new JsonArray();
		if (this.dedupeBy == "idField"){
			int i;
			for(i = 0; i < ids.length; i++){
				JsonObject jo = new JsonObject()
								.add("id", ids[i]);
				inputArray.add(jo);
			}
		}else{
			int i;
			for(i = 0; i < externalIds.length; i++){
				JsonObject jo = new JsonObject()
									.add("externalopportunityid", externalIds[i]);
				inputArray.add(jo);
			}
		}
		requestBody.add("input", inputArray);
		return requestBody;
	}

	public String getToken(){
		String token = null;
		try {
			URL url = new URL(idEndpoint);
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("accept", "application/json");
            int responseCode = urlConn.getResponseCode();
            if (responseCode == 200) {
                InputStream inStream = urlConn.getInputStream();
                Reader reader = new InputStreamReader(inStream);
                JsonObject jsonObject = JsonObject.readFrom(reader);
                token = jsonObject.get("access_token").asString();
            }else {
                throw new IOException("Status: " + responseCode);
            }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
            e.printStackTrace();
        }
		return token;
	}

    private String convertStreamToString(InputStream inputStream) {

        try {
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}
