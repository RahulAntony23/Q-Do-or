package com.example.qdoor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateQRActivity extends AppCompatActivity {

    private TextView qrCodeTV;
    private ImageView qrCodeIV;
    private TextInputEditText dataEdt;
    private Button generateBtn;


    private Button logoutBtn;

    private TextView titleTV;

    private String fname = "";

    private String ffname = "" ;



    GoogleSignInOptions gso;
    GoogleSignInClient gsc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qractivity);
        qrCodeTV = findViewById(R.id.idTVQRGen);
        qrCodeIV = findViewById(R.id.idIVQRCode);
        dataEdt = findViewById(R.id.idEdtData);
        generateBtn = findViewById(R.id.idBtnGenerate);
        logoutBtn = findViewById(R.id.idlogout);
        titleTV = findViewById(R.id.title);



        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            titleTV.setText("Welcome " + signInAccount.getDisplayName());
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsc.signOut();
                finish();
            }
        });

        String en = signInAccount.getDisplayName();
        String[] parts = en.split(" ");
        fname = parts[0]; // 004





        //QR Part
        generateBtn.setOnClickListener(v->{
            String data = dataEdt.getText().toString();
            if(data.isEmpty()){
                Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show();
            }else{
               String t = dataEdt.getText().toString();
               String text = fname + ffname + t;
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;
                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, smallerDimension, smallerDimension);
                    int width1 = bitMatrix.getWidth();
                    int height1 = bitMatrix.getHeight();
                    int[] pixels = new int[width1 * height1];
                    for (int y = 0; y < height1; y++) {
                        int offset = y * width1;
                        for (int x = 0; x < width1; x++) {
                            pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.black):getResources().getColor(R.color.white);
                        }
                    }
                    qrCodeIV.setImageBitmap(Bitmap.createBitmap(pixels, 0, width1, width1, height1, Bitmap.Config.ARGB_8888));
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }

        } );


    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(GenerateQRActivity.this, HomeActivity.class));
            }
        }
        );


    }
}