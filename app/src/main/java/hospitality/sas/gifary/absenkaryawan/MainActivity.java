package hospitality.sas.gifary.absenkaryawan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import hospitality.sas.gifary.absenkaryawan.utils.UserUtil;

public class MainActivity extends AppCompatActivity {
    private ImageView qrCode;
    private Button logOut;
    public final static int QRcodeWidth = 900 ;
    private Bitmap bitmap ;
    private TextView nama,nip;
    private Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcontext = this;

        qrCode = (ImageView) findViewById(R.id.qrCode);
        logOut = (Button) findViewById(R.id.logOut);
        nama =(TextView) findViewById(R.id.nama);
        nip = (TextView) findViewById(R.id.nip);
        nama.setText(UserUtil.getInstance(this).getFullName());
        nip.setText(UserUtil.getInstance(this).getNIK());
        try {
            bitmap = TextToImageEncode(UserUtil.getInstance(this).getNIK()+"-"
                    + UserUtil.getInstance(this).getId()+"-"
                    +UserUtil.getInstance(this).getEmail());

            qrCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUtil.getInstance(mcontext).reset();
                Intent intent = new Intent(mcontext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorPrimary):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 900, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
