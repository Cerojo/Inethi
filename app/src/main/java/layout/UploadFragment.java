package layout;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.locit.cecilhlungwana.inethi.FileUploadService;
import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.Song;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {
    // RecycleView adapter object
    private SimpleItemRecyclerViewAdapter mAdapter;
    // Search edit box
    private EditText searchBox;
    //******************************************
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
    private Runnable runnable;
    private Thread myThread;

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_display, container, false);

        soundList = getPlayList(path);
        songs = getSongs();
        filteredSongs = new ArrayList<Song>();
        filteredSongs.addAll(songs);

        searchBox = (EditText) view.findViewById(R.id.search_box);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.item_list);
        mAdapter = new SimpleItemRecyclerViewAdapter(filteredSongs);
        recyclerView.setAdapter(mAdapter);

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
            runnable = new CountDownRunner();
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
            holder.mDuration.setText(String.valueOf(mValues.get(position).getDuration()));
            holder.mSize.setText(String.valueOf(mValues.get(position).getSize()));
            holder.mShare.setImageResource(R.drawable.upload);
            if(mValues.get(position).getCover()!=null){holder.mImage.setImageBitmap(mValues.get(position).getCover());}
            else{holder.mImage.setImageResource(R.drawable.musicicon);}

            holder.mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("*/*");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(soundList.get(position).get(filePath))));
                    startActivity(Intent.createChooser(sendIntent, "Share"));
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
            song.setSongName(getFileName(i));
            song.setSong(getSong(i));
            song.setArtistName(getFileName(i));
            song.setCover(getCoverArt(i));
            song.setDuration(getDuration(i));
            song.setSize(getFileSize(i));
            songs.add(song);
        }
        return songs;
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


        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FileUploadService test = new FileUploadService();


                String json = test.bowlingJson("Jesse", "Jake");
                System.out.println(json);
                String response = null;
                try {
                    response = test.post("http://192.168.1.103:8080/test", json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        });
        thread.start();*/

//upload();

    /*private void uploadFile(Uri fileUri) {
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void upload(){
        class UploadFileAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                try {
                    String sourceFileUri = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Inethi/" + "Download/new.mp3";//"/mnt/sdcard/abc.png";

                    HttpURLConnection conn = null;
                    DataOutputStream dos = null;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1 * 1024 * 1024;
                    File sourceFile = new File(sourceFileUri);

                    if (sourceFile.isFile()) {

                        try {
                            String upLoadServerUri = "http://locit.co.za/upload.php";//"http://website.com/abc.php?";

                            // open a URL connection to the Servlet
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL(upLoadServerUri);

                            // Open a HTTP connection to the URL
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE",
                                    "multipart/form-data");
                            conn.setRequestProperty("Content-Type",
                                    "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("bill", sourceFileUri);

                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                    + sourceFileUri + "\"" + lineEnd);

                            dos.writeBytes(lineEnd);

                            // create a buffer of maximum size
                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            // read file and write it into form...
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            while (bytesRead > 0) {

                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math
                                        .min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0,
                                        bufferSize);

                            }

                            // send multipart form data necesssary after file
                            // data...
                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens
                                    + lineEnd);

                            // Responses from the server (code and message)
                            serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn
                                    .getResponseMessage();

                            if (serverResponseCode == 200) {

                                // messageText.setText(msg);
                                //Toast.makeText(ctx, "File Upload Complete.",
                                //      Toast.LENGTH_SHORT).show();

                                // recursiveDelete(mDirectory1);

                            }

                            // close the streams //
                            fileInputStream.close();
                            dos.flush();
                            dos.close();

                        } catch (Exception e) {

                            // dialog.dismiss();
                            e.printStackTrace();

                        }
                        // dialog.dismiss();

                    } // End else block


                } catch (Exception ex) {
                    // dialog.dismiss();

                    ex.printStackTrace();
                }
                return "Executed";
            }

            @Override
            protected void onPostExecute(String result) {

            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onProgressUpdate(Void... values) {
            }
        }
        new UploadFileAsync().execute("");
    }*/
