package layout;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.locit.cecilhlungwana.inethi.DbBitmapUtility;
import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.USER;
import com.locit.cecilhlungwana.inethi.UserInfoDBHandler;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends Fragment {
    private ImageView profile;
    public static final int GET_FROM_GALLERY = 3;
    private EditText user;
    private EditText songName;
    private TextView genre;
    private TextView bio;
    private Spinner spinner;
    private EditText bioEdit;
    private Button cancel;
    private ImageButton preview;
    private MediaPlayer beat;

    private final String createBeat = "BEAT";
    private String audioName;
    private Boolean play = true;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private String mName;

    private Button save;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Inethi/" + "UPLOAD/";

    public SaveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        user = (EditText)view.findViewById(R.id.artistNameeditText);
        songName = (EditText)view.findViewById(R.id.songNameeditText);
        profile = (ImageView) view.findViewById(R.id.userimageView);
        genre = (TextView)view.findViewById(R.id.genretextView);
        genre.setVisibility(View.INVISIBLE);
        bio = (TextView)view.findViewById(R.id.biotextView);
        bio.setVisibility(View.INVISIBLE);
        spinner = (Spinner)view.findViewById(R.id.genrespinner);
        spinner.setVisibility(View.INVISIBLE);
        bioEdit = (EditText)view.findViewById(R.id.bioeditText);
        bioEdit.setVisibility(View.INVISIBLE);
        preview = (ImageButton)view.findViewById(R.id.previewimageButton);
        save = (Button)view.findViewById(R.id.savebutton);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", getActivity().MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        if(loginPreferences.getBoolean("saveLogin", false)) {
            mName = loginPreferences.getString("username", "");
            UserInfoDBHandler dbHandler = new UserInfoDBHandler(getContext(), null, null, 1);
            USER USER = dbHandler.findUser(mName);
            user.setText(USER.getName());
            profile.setImageBitmap(DbBitmapUtility.getImage(USER.getImage()));
        }

        if(getArguments()!=null) {
            audioName = getArguments().getString(createBeat);
            beat = MediaPlayer.create(getContext(), Uri.fromFile(new File(audioName)));
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(play){
                        beat.start();
                        preview.setImageResource(R.drawable.pause);
                    }
                    else {
                        beat.pause();
                        preview.setImageResource(R.drawable.play);
                    }
                    play = !play;
                }
            });
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // File (or directory) with old name
                File file = new File(audioName);

                // File (or directory) with new name
                File file2 = new File(path+songName.getText().toString()+".mp3");

                if (file2.exists()) {
                    Toast.makeText(getContext(),"file exists",Toast.LENGTH_LONG).show();
                }
                else{
                    // Rename file (or directory)
                    boolean success = file.renameTo(file2);

                    if (!success) {
                        // File was not successfully renamed
                        Toast.makeText(getContext(),"failed to save",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Saved", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        cancel = (Button)view.findViewById(R.id.cancelbutton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == getActivity().RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                profile.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
