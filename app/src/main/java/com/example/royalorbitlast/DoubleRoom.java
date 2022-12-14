package com.example.royalorbitlast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoubleRoom extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_double_room2);

DatabaseReference myRef = database.getReference("Room Reservations");

        final EditText phoneDouble = findViewById(R.id.phoneDouble);
        final EditText noRoomDouble = findViewById(R.id.noRoomDouble);
        final EditText inDateDouble = findViewById(R.id.inDateDouble);
        final EditText dDouble = findViewById(R.id.dDouble);



        final Button confirmDouble = findViewById(R.id.confirmDouble);

        confirmDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from edit text to string value
                final String phoneTxtD = phoneDouble.getText().toString().trim();
                final String noRoomTxtD = noRoomDouble.getText().toString();
                final String inDateTxtD = inDateDouble.getText().toString();
                final String dTxtD = dDouble.getText().toString().trim();

                final Double Rooms= Double.parseDouble(noRoomTxtD);
                final Double dates= Double.parseDouble(dTxtD);

                final Double price = Rooms * dates * 10000;
                final String  pr = String.valueOf(price);
                final String type = "Double Room";


                if (phoneTxtD.isEmpty()) {
                    phoneDouble.setError("phone no. is Required");
                    phoneDouble.requestFocus();
                    return;
                }
                if (noRoomTxtD.isEmpty()) {
                    noRoomDouble.setError("No of Rooms Required");
                    noRoomDouble.requestFocus();
                    return;
                }
                if (inDateTxtD.isEmpty()) {
                    inDateDouble.setError("Check In Date Required");
                    inDateDouble.requestFocus();
                    return;
                }
                if(dates < 0 ){
                    dDouble.setError("No of Dates cannot be zero");
                    inDateDouble.requestFocus();
                    return;
                }
                if(Rooms > 10 ){
                    noRoomDouble.setError("Maximum Number Of Booking Room is 10");
                    noRoomDouble.requestFocus();
                    return;
                }
                else{
                    myRef.child("double").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //send data to databse
                            myRef.child("double").child(phoneTxtD).child("No Of Rooms").setValue(noRoomTxtD);
                            myRef.child("double").child(phoneTxtD).child("Check in date").setValue(inDateTxtD);
                            myRef.child("double").child(phoneTxtD).child("No of days").setValue(dTxtD);
                            myRef.child("double").child(phoneTxtD).child("price").setValue(price);

                            String DateFromDb = snapshot.child(phoneTxtD).child("Check in date").getValue(String.class);
                            String RoomFromDb = snapshot.child(phoneTxtD).child("No Of Rooms").getValue(String.class);
                            String noDaysFromDB = snapshot.child(phoneTxtD).child("No of days").getValue(String.class);
                            String priceD = pr;
                            String typeD = type;

                            Intent intent1 = new Intent(getApplicationContext(),ReservedRoomDetailsDouble.class);

                            intent1.putExtra("No of Rooms",RoomFromDb);
                            intent1.putExtra("Check in date",DateFromDb);
                            intent1.putExtra("No of days",noDaysFromDB);
                            intent1.putExtra("price",priceD);
                            intent1.putExtra("type",typeD);

                            startActivity(intent1);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
    }
}