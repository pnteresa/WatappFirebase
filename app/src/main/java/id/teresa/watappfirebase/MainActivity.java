package id.teresa.watappfirebase;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Message> messageList = new ArrayList<Message>();

    private DatabaseReference mRef;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(getBaseContext(), messageList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton mSend = (FloatingActionButton) findViewById(R.id.send_button);
        final String TAG = "Main";

        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://watappfirebase.firebaseio.com/");
        mDatabase = mRef.child("messages");

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message m = new Message("LOL", Constants.MY_NAME);
                add(m);
            }
        });
        addDummyMessages();

//        public void scrollToBottom(){
//            recyclerView.scrollVerticallyTo(0);
//        }
    }

    private void addDummyMessages() {
        add(new Message("Knock knock", Constants.MY_NAME));
        add(new Message("Who's there?", "other"));
        add(new Message("Harry", Constants.MY_NAME));
        add(new Message("Harry who?", "other"));
        add(new Message("Harry up it's cold up here!", Constants.MY_NAME));
    }

    private void add(Message m) {
        String key = mDatabase.push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = m.toMap();
        childUpdates.put(key, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
