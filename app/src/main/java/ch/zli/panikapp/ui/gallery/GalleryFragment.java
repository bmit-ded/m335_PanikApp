package ch.zli.panikapp.ui.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ch.zli.panikapp.R;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

            galleryViewModel =
                    new ViewModelProvider(this).get(GalleryViewModel.class);
            View root = inflater.inflate(R.layout.fragment_gallery, container, false);
            ImageView imageView = (ImageView) root.findViewById(R.id.imageView);
            TextView textView = (TextView) root.findViewById(R.id.textView1);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            File file = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            Date lastModDate = new Date(file.lastModified());
            Bitmap bitmap = BitmapFactory.decodeFile(getLatestPicture(file.toString()));
            textView.setText("Zeit des Angriffs: " + lastModDate);
            imageView.setImageBitmap(bitmap);
        } else {
            while (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //falls nicht erteilt
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 44);
            }

        }


            return root;



    }


        public String getLatestPicture (String dirPath){

            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                return null;
            }

            File lastModifiedFile = files[0];
            for (int i = 1; i < files.length; i++) {
                if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                    lastModifiedFile = files[i];
                }
            }
            return lastModifiedFile.toString();
        }


}