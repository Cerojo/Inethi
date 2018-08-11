package layout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {
    private SimpleItemRecyclerViewAdapter mAdapter;
    private final String fileName = "file_name";
    private final String filePath = "file_path";
    private ArrayList<HashMap<String, String>> soundList;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Inethi/" + "UPLOAD/";
    private final String fileFormat = ".mp3";
    private List<Song> songs;
    private List<Song> filteredSongs;
    private MediaPlayer song;
    private int counter = 0;
    private boolean threadRunning = false;
    private static boolean state = false;
    private Thread myThread;
    private String urlSong = "http://172.20.10.3:8080/uploadSong";
    private String url = "http://172.20.10.3:8080/upload";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public UploadFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_display, container, false);

        soundList = getPlayList(path);
        songs = getSongs();
        filteredSongs = new ArrayList<Song>();
        filteredSongs.addAll(songs);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.item_list);
        mAdapter = new SimpleItemRecyclerViewAdapter(filteredSongs);
        recyclerView.setAdapter(mAdapter);

        search(view);
        return view;
    }

    private void search(View view) {
        EditText searchBox = (EditText) view.findViewById(R.id.search_box);
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
    }

    //Upload a song to server
    private void upload(String... value){
        final String IMGUR_CLIENT_ID = "9199fdef135c122";
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Upload")
                .addFormDataPart("song", value[0], RequestBody.create(MediaType.parse("audio/mp3"), new File(value[1])))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Response response = client.newCall(request).execute()) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Upload user information to server
    private void postDetails(String... values) throws IOException {
         org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
         obj.put("songName", values[0]);
         obj.put("artistName", values[1]);
         obj.put("size", values[2]);
         obj.put("duration", values[3]);

        RequestBody body = RequestBody.create(JSON, obj.toJSONString());
        Request request = new Request.Builder()
                .url(urlSong)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()){
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    //Handles item view
    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {
        private List<Song> mValues;
        private CustomFilter mFilter;
        private Boolean playing = true;
        private int previousSong = -1;
        private ImageButton previousB;
        private ViewHolder vHolder;

        SimpleItemRecyclerViewAdapter(List<Song> items) {
            mValues = items;
            mFilter = new CustomFilter(SimpleItemRecyclerViewAdapter.this);
            Runnable runnable = new CountDownRunner();
            myThread = new Thread(runnable);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_view_layout, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mName.setText(String.valueOf(mValues.get(position).getArtistName()));
            holder.mSName.setText(String.valueOf(mValues.get(position).getSongName()));
            holder.mDuration.setText(String.valueOf(mValues.get(position).getDuration()));
            holder.mSize.setText(String.valueOf(mValues.get(position).getSize()));
            holder.mShare.setImageResource(R.drawable.upload);
            if(mValues.get(position).getCover()!=null){holder.mImage.setImageBitmap(mValues.get(position).getCover());}
            else{holder.mImage.setImageResource(R.drawable.musicicon);}

            holder.mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"Uploading...",Toast.LENGTH_LONG).show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                postDetails(
                                        holder.mItem.getSongName(),
                                        holder.mItem.getArtistName(),
                                        holder.mItem.getSize(),
                                        holder.mItem.getDuration());
                                upload(holder.mItem.getSongName(),
                                        soundList.get(position).get(filePath));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
            });

            holder.mPlay.setImageResource(R.drawable.play);
            final boolean check = song!=null && song.isPlaying() && !song.equals(holder.mItem.getSong());
            if(check){
                holder.mPlay.setImageResource(R.drawable.play);
            }
            if(previousSong == position){
                holder.mPlay.setImageResource(R.drawable.pause);
            }

            holder.mPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((song!=null) && (song.isPlaying()) && (holder.getAdapterPosition() != previousSong)){
                        song.pause();
                        getPreviousB().setImageResource(R.drawable.play);
                        setPreviousB(null);
                        vHolder.mDuration.setText(getDuration(previousSong));
                        playing = !playing;
                        if(myThread!=null){
                            threadRunning = false;
                        }
                    }

                    if(playing){
                        holder.mPlay.setImageResource(R.drawable.pause);
                        song = holder.mItem.getSong();
                        setPreviousB(holder.mPlay);
                        previousSong = position;
                        song.start();
                        vHolder = holder;
                        counter = 0;
                        if(!state) {
                            myThread.start();
                            state = true;
                        }
                        threadRunning = true;
                    }
                    else{
                        holder.mPlay.setImageResource(R.drawable.play);
                        song.pause();
                        if(myThread!=null){
                            threadRunning = false;
                        }
                    }
                    playing = !playing;
                }
            });
        }

        private void setPreviousB(ImageButton button){
            previousB = button;
        }
        private ImageButton getPreviousB(){
            return previousB;
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
            final TextView mSName;
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
                mSName = (TextView) view.findViewById(R.id.songNametextView);
                mDuration = (TextView) view.findViewById(R.id.durationtextView);
                mSize = (TextView) view.findViewById(R.id.sizetextView);
                mImage = (ImageView) view.findViewById(R.id.songCoverimageView);
                mPlay = (ImageButton) view.findViewById(R.id.playimageButton);
                mShare = (ImageButton) view.findViewById(R.id.downloadimageButton);
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
                    String d;
                    int min_to_sec = song.getDuration()/1000 - counter;
                    if(min_to_sec >= 0) {
                        int m = min_to_sec / 60;
                        int s = min_to_sec - m * 60;
                        if (s < 10) {
                            d = m + ":0" + s;
                        } else {
                            d = m + ":" + s;
                        }

                        vHolder.mDuration.setText(d);
                        counter++;
                    }
                    else{
                        threadRunning = false;
                        playing = !playing;
                        counter = 0;
                        getPreviousB().setImageResource(R.drawable.play);
                        vHolder.mDuration.setText(getDuration(previousSong));
                    }
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
        for (int i = 0; i < soundList.size(); i++) {
            Song song = new Song();
            song.setSongName(songName(i));
            song.setSong(getSong(i));
            song.setArtistName(artistName(i));
            song.setCover(getCoverArt(i));
            song.setDuration(getDuration(i));
            song.setSize(getFileSize(i));
            songs.add(song);
        }
        return songs;
    }

    private String songName(int index){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = soundList.get(index).get(filePath);
        if (path != null) {
            mmr.setDataSource(path);
            String s = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if(s != null){
                return s;
            }
            return soundList.get(index).get(fileName).split(fileFormat)[0];
        }
        return soundList.get(index).get(fileName).split(fileFormat)[0];
    }

    private String artistName(int index){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = soundList.get(index).get(filePath);
        if (path != null) {
            mmr.setDataSource(path);
            String s = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if(s != null){
                return s;
            }
            return soundList.get(index).get(fileName).split(fileFormat)[0];
        }
        return soundList.get(index).get(fileName).split(fileFormat)[0];
    }

    public ArrayList<HashMap<String, String>> getPlayList(String index) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
        try {
            File rootFolder = new File(index);
            File[] files = rootFolder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(fileFormat)) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put(filePath, file.getAbsolutePath());
                    song.put(fileName, file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

    public String getFileName(int index) {
        return soundList.get(index).get(fileName).split(fileFormat)[0];
    }

    public MediaPlayer getSong(int index) {
        String musicPath = soundList.get(index).get(filePath);
        return MediaPlayer.create(getContext(), Uri.parse(musicPath));
    }

    public Bitmap getCoverArt(int index) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = soundList.get(index).get(filePath);
        Bitmap bitmap = null;
        if (path != null) {
            mmr.setDataSource(path);
            byte[] data = mmr.getEmbeddedPicture();

            if (data != null) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        }
        return bitmap;
    }

    public String getDuration(int duration) {
        String soundPath = soundList.get(duration).get(filePath);
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(soundPath));
        int min_to_sec = mediaPlayer.getDuration() / 1000;
        int m = min_to_sec / 60;
        int s = min_to_sec - m * 60;
        if (s < 10) {
            return m + ":0" + s;
        }
        return m + ":" + s;
    }

    public String getFileSize(int index) {
        File file = new File(soundList.get(index).get(filePath));
        float fileSize = (float) (file.length() / 1000000.0);
        return Math.round(fileSize * 100.0) / 100.0 + "MB";
    }

    @Override
    public void onResume() {
        super.onResume();
        if(song!=null){
            song.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if(song!=null){
                song.pause();
                threadRunning = false;
            }
        }
        catch (Exception ignored){}
    }
}