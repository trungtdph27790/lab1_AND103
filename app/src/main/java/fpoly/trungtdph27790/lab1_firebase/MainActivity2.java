package fpoly.trungtdph27790.lab1_firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {
    FirebaseFirestore database;

    Button btnInsert,btnUpdate,btnDelete,btnShow;
    TextView tvResult ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseFirestore.getInstance(); // khởi tạo database

        tvResult = findViewById(R.id.demo_fireBase);
        btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertFireBase(tvResult);
            }
        });
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFireBase(tvResult);
            }
        });
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFireBase(tvResult);
            }
        });
        btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showData(tvResult);
            }
        });


    }



    String id = "" ;
    ToDo toDo = null ;
    public void insertFireBase(TextView tvResult){
        // id được tự tạo
        id = UUID.randomUUID().toString();
        toDo = new ToDo(id,"title new","content new"); // tạo đối tưựng để insert
        //chuyển đổi sang đối tượng có thể thao tác với firebase
        HashMap<String,Object> map = toDo.convertHashMap();
        // insert vào database
        database.collection("TODO").document(id)
                .set(map) // đối tượng cần insert
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Thêm thành công");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });

    }
    public void updateFireBase(TextView tvResult){
        id = "4fd0240a-a89c-479e-b1c9-ac700e0aedfc";
        toDo = new ToDo(id,"sua title new","conten new");
        database.collection("TODO").document(toDo.getId())
                .update(toDo.convertHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Sửa thành công");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }
    public void deleteFireBase(TextView tvResult){
        id = "4fd0240a-a89c-479e-b1c9-ac700e0aedfc" ;
        database.collection("TODO").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Xóa thành công");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }
    String strResult = "" ;
    public ArrayList<ToDo> showData(TextView tvResult){
        ArrayList<ToDo> list = new ArrayList<>();
        database.collection("TODO").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            strResult = "";
                            // đọc theo tung dòng dữ liệu
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                //chuyển dữ liệu của dòng sang đối tượng
                                ToDo toDo1 = documentSnapshot.toObject(ToDo.class);
                                // chuyển đối tượng thành chuỗi list thành chuỗi
                                strResult += "Id: "+toDo1.getId()+"\n";
                                list.add(toDo1); // thêm vào list
                            }
                            //hiển thị kết quá
                            tvResult.setText(strResult);
                        }else {
                            tvResult.setText("Đọc dữ liệu thất bại");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
        return list ;
    }
}