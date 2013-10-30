package com.example.audiolist;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;

public class AudioListActivity extends Activity {
	
	private final String urlstring = 
			"http://mp3poolonline.com/rest/views/view_allaudio?display_id=page_14";
	//private HttpURLConnection urlConnection;
	//private BufferedReader br;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.audio_list);
		JSONParser parser = new JSONParser();
		parser.execute(urlstring);
		ListView audio_listview = (ListView)findViewById(R.id.list);
		//audio_listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getDataFromUrl()));
		audio_listview.setAdapter(adapter);		
	}
	
	/*public ArrayList<String> getDataFromUrl(){
		ArrayList<String> urlItems = new ArrayList<String>();
		try{
		URL url = new URL(urlstring);
		urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.connect();
		if(urlConnection != null)
			Log.d("MyDEBUG", "Connected");
		
		br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String next = null;
			while((next = br.readLine()) != null){
				Log.d("MyDEBUG","BufferedReader readline" + next);
				JSONArray jArray = new JSONArray(next);
				for(int i = 0; i<jArray.length();i++){
					JSONObject jObject = jArray.getJSONObject(i);
					urlItems.add(jObject.getString("title"));
				}
			}
		}catch(MalformedURLException exc){
			exc.printStackTrace();
			Log.d("MyDEBUG","Malformated exc" + exc.toString());
		}
		 catch(IOException exc){
			 exc.printStackTrace();
			 Log.d("MyDEBUG","IOExc" + exc.toString());
		}
		 catch(JSONException exc){
			 exc.printStackTrace();
			 Log.d("MyDEBUG","JSONExc" + exc.toString());
		}finally{
			if(urlConnection != null)
				urlConnection.disconnect();
			try{
			if(br != null)
				br.close();
			}catch(IOException exc){
				exc.printStackTrace();
			}
		}
		 
		return urlItems;
	}*/

	private class JSONParser extends AsyncTask<String, Void, List<String>>{

		@Override
		protected List<String> doInBackground(String... params) {
			List<String> urlItems = new ArrayList<String>();
			try{
				URL url = new URL(params[0]);
				Log.d("MyDebug", "URL ConnString" + params[0]);
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();
				Log.d("MyDebug", "Connected");
				InputStream instream = urlConnection.getInputStream();
				
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(instream));
					StringBuilder strBuilder = new StringBuilder();
					String line = null;
					while((line = bufReader.readLine()) !=null){
						strBuilder.append(line);
					}
					
				String JSONString = strBuilder.toString();
				Log.d("MyDebug", "JSONString" + strBuilder.toString());
				JSONArray jArray = new JSONArray(JSONString);
					for(int i = 0;i<jArray.length();i++){
						urlItems.add(convertData(jArray.getJSONObject(i)));
					}
				return urlItems;
			}catch(Exception exc){
				Log.d("MyDebug","Try exception"+exc);
			}
				
			return null;
		}
		
		@Override
		protected void onPostExecute(List<String> urlItems){
			super.onPostExecute(urlItems);
			adapter = new ArrayAdapter<String>(AudioListActivity.this,android.R.layout.simple_list_item_1,urlItems);
		}
		
		private String convertData(JSONObject jObject) throws JSONException{
			String title = jObject.getString("title");
			return title;
		}
	}
}
