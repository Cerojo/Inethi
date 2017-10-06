package layout;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.locit.cecilhlungwana.inethi.R;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends Fragment {
    private ImageView profile;
    public static final int GET_FROM_GALLERY = 3;
    private TextView genre;
    private TextView bio;
    private Spinner spinner;
    private EditText bioEdit;
    private Button cancel;

    private final String createBeat = "BEAT";
    private String audioName;

    public SaveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        genre = (TextView)view.findViewById(R.id.genretextView);
        genre.setVisibility(View.INVISIBLE);
        bio = (TextView)view.findViewById(R.id.biotextView);
        bio.setVisibility(View.INVISIBLE);
        spinner = (Spinner)view.findViewById(R.id.genrespinner);
        spinner.setVisibility(View.INVISIBLE);
        bioEdit = (EditText)view.findViewById(R.id.bioeditText);
        bioEdit.setVisibility(View.INVISIBLE);

        cancel = (Button)view.findViewById(R.id.cancelbutton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        profile = (ImageView) view.findViewById(R.id.userimageView);
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
