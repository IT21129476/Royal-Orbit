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

public class SingleRoom extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_single_room);

        DatabaseReference myRef = database.getReference("Room Reservations");

        final EditText phoneSingle = findViewById(R.id.phoneSingle);
        final EditText noRoomSingle = findViewById(R.id.noRoomSingle);
        final EditText inDateSingle = findViewById(R.id.inDateSingle);
        final EditText dSingle = findViewById(R.id.dSingle);

        final Button confirmS = findViewById(R.id.confirmS);

        confirmS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from edit text to string value
                final String phoneTxtS = phoneSingle.getText().toString().trim();
                final String noRoomTxtS = noRoomSingle.getText().toString();
                final String inDateQueenTxtS = inDateSingle.getText().toString();
                final String dQueenTxtS= dSingle.getText().toString();
//
//
                final Double Rooms= Double.parseDouble(noRoomTxtS);
                final Double dates= Double.parseDouble(dQueenTxtS);


                final Double price = Rooms * dates * 5000;
                final String  pr = String.valueOf(price);
                final String type = "Single Room";

                if (phoneTxtS.isEmpty()) {
                    phoneSingle.setError("phone no. is Required");
                    phoneSingle.requestFocus();
                    return;
                }

                if (noRoomTxtS.isEmpty()) {
                    noRoomSingle.setError("No of Rooms Required");
                    noRoomSingle.requestFocus();
                    return;
                }

//                if (!Patterns.(00/00/0000).matcher(inDateQueen).matches()) {
//                    inDateQueen.setError("please provide valid email");
//                    inDateQueen.requestFocus();
//                    return;
//                }

                if (inDateQueenTxtS.isEmpty()) {
                    inDateSingle.setError("Check In Date Required");
                    inDateSingle.requestFocus();
                    return;
                }


                if(dates < 0 ){
                    dSingle.setError("No of Dates cannot be zero");
                    inDateSingle.requestFocus();
                    return;
                }

                if(Rooms > 10 ){
                    noRoomSingle.setError("Maximum Number Of Booking Room is 10");
                    noRoomSingle.requestFocus();
                    return;
                }



                else {
                    myRef.child("single").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //send data to databse
                            myRef.child("single").child(phoneTxtS).child("No Of Rooms").setValue(noRoomTxtS);
                            myRef.child("single").child(phoneTxtS).child("Check in date").setValue(inDateQueenTxtS);
                            myRef.child("single").child(phoneTxtS).child("No of days").setValue(dQueenTxtS);
                            myRef.child("single").child(phoneTxtS).child("price").setValue(price);


                            String DateFromDb = snapshot.child(phoneTxtS).child("Check in date").getValue(String.class);
                            String RoomFromDb = snapshot.child(phoneTxtS).child("No Of Rooms").getValue(String.class);
                            String noDaysFromDB = snapshot.child(phoneTxtS).child("No of days").getValue(String.class);
                            String priceS = pr;
                            String typeS = type;

                            Intent intent = new Intent(getApplicationContext(),ReservedRoomDetailsSingle.class);

                            intent.putExtra("No of Rooms",RoomFromDb);
                            intent.putExtra("Check in date",DateFromDb);
                            intent.putExtra("No of days",noDaysFromDB);
                            intent.putExtra("price",priceS);
                            intent.putExtra("type",typeS);


                            Toast.makeText(SingleRoom.this, "Rooms Booked Succesful", Toast.LENGTH_LONG).show();
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