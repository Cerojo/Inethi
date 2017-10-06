package layout;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.Song;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {
    private ArrayList<HashMap<String, String>> songList = new ArrayList<>();
    private JSONArray songsJSON = null;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_SONG = "songName";
    private static final String TAG_NAME = "artistName";
    private static final String TAG_SIZE = "size";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_FILE = "song";
    private static final String TAG_ID = "id";

    private SimpleItemRecyclerViewAdapter mAdapter;
    // Search edit box
    private EditText searchBox;
    //******************************************
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Inethi/" + "MUSIC/";
    private List<Song> songs;
    private List<Song> filteredSongs;
    private int counter = 0;
    private boolean threadRunning = false;
    private Runnable runnable;
    private Thread myThread;
    private MediaPlayer player;
    private boolean state = true;

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_display, container, false);
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        getSongsServer();
        //getDownload();

        searchBox = (EditText) view.findViewById(R.id.search_box);

        // search suggestions using the edittext widget
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void getSongsServer() {
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
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                showList(result);
                songs = getSongs();
                filteredSongs = new ArrayList<Song>();
                filteredSongs.addAll(songs);

                RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.item_list);
                mAdapter = new SimpleItemRecyclerViewAdapter(filteredSongs);
                recyclerView.setAdapter(mAdapter);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    private void showList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            songsJSON = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < songsJSON.length(); i++) {
                JSONObject c = songsJSON.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String song = c.getString(TAG_SONG);
                String name = c.getString(TAG_NAME);
                String size = c.getString(TAG_SIZE);
                String duration = c.getString(TAG_DURATION);
                String file = c.getString(TAG_FILE);

                HashMap<String, String> songs = new HashMap<String, String>();

                songs.put(TAG_ID, id);
                songs.put(TAG_SONG, song);
                songs.put(TAG_NAME, name);
                songs.put(TAG_SIZE, size);
                songs.put(TAG_DURATION, duration);
                songs.put(TAG_FILE, file);
                songList.add(songs);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getDownload() {
        class DownloadFile extends AsyncTask<Void, String, File> {

            /**
             * Before starting background thread Show Progress Bar Dialog
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //showProgressDialog();
                Toast.makeText(getContext(),"Downloading...",Toast.LENGTH_SHORT).show();
            }

            /**
             * Downloading file in background thread
             */
            @Override
            protected File doInBackground(Void... params) {
                int count;
                File file = null;
                String link = "http://196.47.253.131:3003/music?id=hello2.mp3";//"http://196.47.253.131:3003/music?id=hello.mp3";
                try {
                    URL url = new URL(link);
                    URLConnection conection = url.openConnection();
                    conection.connect();

                    int lenghtOfFile = conection.getContentLength();

                    InputStream input = conection.getInputStream();

                    player.setDataSource(link);
                    player.prepare();
                    player.start();

                    //file = getFile(lenghtOfFile, input);

                } catch (Exception e) {
                    return null;
                }
                return file;
            }

            @NonNull
            private File getFile(int lenghtOfFile, InputStream input) throws IOException {
                File file;
                int count;
                File folder = new File(path);
                if (!folder.exists())
                    folder.mkdir();
                file = new File(folder, "hello1.mp3");

                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    output.write(data, 0, count);
                }

                output.flush();

                output.close();
                input.close();
                return file;
            }

            /**
             * Updating progress bar
             */
            protected void onProgressUpdate(String... progress) {
                // setting progress percentage
                //mProgressDialog.setProgress(Integer.parseInt(progress[0]));
            }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            @Override
            protected void onPostExecute(File file) {
                // dismiss the dialog after the file was downloaded
                //dismissProgressDialog();
                Toast.makeText(getContext(),"Download complete",Toast.LENGTH_SHORT).show();
            }

        }
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.execute();
    }

    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {
        private List<Song> mValues;
        private CustomFilter mFilter;

        SimpleItemRecyclerViewAdapter(List<Song> items) {
            mValues = items;
            mFilter = new CustomFilter(SimpleItemRecyclerViewAdapter.this);
            runnable = new CountDownRunner();
            myThread = new Thread(runnable);
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_view_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mName.setText(String.valueOf(mValues.get(position).getArtistName()));
            holder.mDuration.setText(String.valueOf(mValues.get(position).getDuration()));
            holder.mSize.setText(String.valueOf(mValues.get(position).getSize()));
            holder.mShare.setImageResource(R.drawable.download);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mName;
            final TextView mDuration;
            final TextView mSize;
            final ImageView mImage;
            final ImageButton mPlay;
            final ImageButton mShare;
            Song mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mName = (TextView) view.findViewById(R.id.artistNametextView);
                mDuration = (TextView) view.findViewById(R.id.durationtextView);
                mSize = (TextView) view.findViewById(R.id.sizetextView);
                mImage = (ImageView) view.findViewById(R.id.songCoverimageView);
                mPlay = (ImageButton) view.findViewById(R.id.playimageButton);
                mShare = (ImageButton) view.findViewById(R.id.downloadimageButton);
                mShare.setImageResource(R.drawable.download);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mName.getText() + "'";
            }
        }

        class CustomFilter extends Filter {
            private SimpleItemRecyclerViewAdapter mAdapter;

            private CustomFilter(SimpleItemRecyclerViewAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredSongs.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredSongs.addAll(songs);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final Song mSong : songs) {
                        if (mSong.getSongName().toLowerCase().contains(filterPattern) || mSong.getArtistName().toLowerCase().contains(filterPattern)) {
                            filteredSongs.add(mSong);
                        }
                    }
                }
                System.out.println("Count Number " + filteredSongs.size());
                results.values = filteredSongs;
                results.count = filteredSongs.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println("Count Number 2 " + ((List<Song>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }

        private void doWork() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                }
            });
        }

        class CountDownRunner implements Runnable{
            public void run() {
                while(threadRunning){
                    try {
                        doWork();
                        Thread.sleep(1000); //522 - 750 BPM, Increment by 23
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }catch(Exception ignored){}
                }
            }
        }
    }

    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < songList.size(); i++) {
            Song song = new Song();
            HashMap<String, String> online = songList.get(i);
            song.setSongName(online.get(TAG_SONG));
            song.setSong(null);
            song.setArtistName(online.get(TAG_SONG));
            song.setCover(null);
            song.setDuration(online.get(TAG_DURATION));
            song.setSize(online.get(TAG_SIZE));
            songs.add(song);
        }
        return songs;
    }
}
