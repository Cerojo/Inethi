package layout;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.locit.cecilhlungwana.inethi.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {
    private ArrayList<HashMap<String, String>> songList = new ArrayList<>();
    private JSONArray songs = null;

    private static final String TAG_RESULTS="result";
    private static final String TAG_SONG="songName";
    private static final String TAG_NAME="artistName";
    private static final String TAG_SIZE="size";
    private static final String TAG_DURATION="duration";
    private static final String TAG_FILE="song";
    private static final String TAG_ID = "id";


    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_display, container, false);
        getSongs();
        return view;
    }

    private void getSongs(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://cartoclock.com/beats");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                showList(result);
                Toast.makeText(getContext(),songList.get(0).get(TAG_NAME),Toast.LENGTH_LONG).show();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    private void showList(String myJSON){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            songs = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<songs.length();i++){
                JSONObject c = songs.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String song = c.getString(TAG_SONG);
                String name = c.getString(TAG_NAME);
                String size = c.getString(TAG_SIZE);
                String duration = c.getString(TAG_DURATION);
                String file = c.getString(TAG_FILE);

                HashMap<String,String> songs = new HashMap<String,String>();

                songs.put(TAG_ID,id);
                songs.put(TAG_SONG,song);
                songs.put(TAG_NAME,name);
                songs.put(TAG_SIZE,size);
                songs.put(TAG_DURATION,duration);
                songs.put(TAG_FILE,file);
                songList.add(songs);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
