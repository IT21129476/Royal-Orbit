package com.example.royalorbitlast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QueensRoom extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_queens_room);

        DatabaseReference myRef = database.getReference("Room Reservations");

        final EditText phoneQueen = findViewById(R.id.phoneQueen);
        final EditText noRoomQueen = findViewById(R.id.noRoomQueen);
        final EditText inDateQueen = findViewById(R.id.inDateQueen);
        final EditText dQueen = findViewById(R.id.dQueen);

        final Button confirmQ = findViewById(R.id.confirmQ);

        confirmQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from edit text to string value
                final String phoneTxtQ = phoneQueen.getText().toString().trim();
                final String noRoomTxtQ = noRoomQueen.getText().toString();
                final String inDateQueenTxtQ = inDateQueen.getText().toString();
                final String dQueenTxtQ= dQueen.getText().toString();
//
//
                final Double Rooms= Double.parseDouble(noRoomTxtQ);
                final Double dates= Double.parseDouble(dQueenTxtQ);


                final Double price = Rooms * dates * 15000;
                final String  pr = String.valueOf(price);
                final String type = "Queen Room";

                if (phoneTxtQ.isEmpty()) {
                    phoneQueen.setError("phone no. is Required");
                    phoneQueen.requestFocus();
                    return;
                }

               if (noRoomTxtQ.isEmpty()) {
                    noRoomQueen.setError("No of Rooms Required");
                    noRoomQueen.requestFocus();
                    return;
                }

//                if (!Patterns.(00/00/0000).matcher(inDateQueen).matches()) {
//                    inDateQueen.setError("please provide valid email");
//                    inDateQueen.requestFocus();
//                    return;
//                }

              if (inDateQueenTxtQ.isEmpty()) {
                   inDateQueen.setError("Check In Date Required");
                    inDateQueen.requestFocus();
                    return;
                }


                if(dates < 0 ){
                    dQueen.setError("No of Dates cannot be zero");
                    inDateQueen.requestFocus();
                    return;
                }

                if(Rooms > 10 ){
                    noRoomQueen.setError("Maximum Number Of Booking Room is 10");
                    noRoomQueen.requestFocus();
                    return;
                }



else {
                    myRef.child("Queen").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //send data to databse
                            myRef.child("Queen").child(phoneTxtQ).child("No Of Rooms").setValue(noRoomTxtQ);
                            myRef.child("Queen").child(phoneTxtQ).child("Check in date").setValue(inDateQueenTxtQ);
//                            myRef.child("Queen").child(phoneTxtQ).child("Check out date").setValue(outDateQueenTxtQ);
                            myRef.child("Queen").child(phoneTxtQ).child("No of days").setValue(dQueenTxtQ);
                            myRef.child("Queen").child(phoneTxtQ).child("price").setValue(price);


                            String DateFromDb = snapshot.child(phoneTxtQ).child("Check in date").getValue(String.class);
                            String RoomFromDb = snapshot.child(phoneTxtQ).child("No Of Rooms").getValue(String.class);
                            String noDaysFromDB = snapshot.child(phoneTxtQ).child("No of days").getValue(String.class);
                            String priceQ = pr;
                            String typeQ = type;

                            Intent intent = new Intent(getApplicationContext(),ReserveRoomDetailsQueen.class);

                            intent.putExtra("No of Rooms",RoomFromDb);
                            intent.putExtra("Check in date",DateFromDb);
                            intent.putExtra("No of days",noDaysFromDB);
                            intent.putExtra("price",priceQ);
                            intent.putExtra("type",typeQ);


                            Toast.makeText(QueensRoom.this, "Rooms Booked Succesful", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }

            }

        });


    }

    private void openRoomReservation() {
        Intent intent = new Intent(this, RoomReservation.class);
        startActivity(intent);
    }
}