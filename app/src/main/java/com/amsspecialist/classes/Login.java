package com.amsspecialist.classes;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Login extends AsyncTask<String, Void, JSONObject> {
	Context con;
	
	public Login(Context con) {
		
		this.con=con;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String user = params[0];
		String pass = params[1];
		JSONObject obj = null;
		try {
			String us = URLEncoder.encode(user, "UTF-8");
			String passw = URLEncoder.encode(pass, "UTF-8");

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(
					GlobalUtilities.URLLOGIN);

			try {
				// Add user name and password

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("username", us));
				nameValuePairs.add(new BasicNameValuePair("password", passw));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				
				HttpResponse response = httpclient.execute(httppost);

				String sstream = convertInputStreamToString(
						response.getEntity().getContent()).toString();
				try {
					obj = new JSONObject(sstream);

					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}

		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			
		}
		return obj;
	}

	@Override
	protected void onPostExecute(JSONObject js) {
		// TODO Auto-generated method stub
		super.onPostExecute(js);

	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

}
