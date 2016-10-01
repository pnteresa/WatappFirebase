package id.teresa.watappfirebase;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Message> messageList = new ArrayList<Message>();

    private DatabaseReference mRef;
    private DatabaseReference mDatabase;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        final EditText et = (EditText) findViewById(R.id.edit_text);

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
                Message m = new Message(et.getText().toString(), Constants.MY_NAME);
                et.setText("");
                add(m);
            }
        });
        addDummyMessages();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Message> td = (HashMap<String, Message>) dataSnapshot.getValue();
                Iterator it = td.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    Map<String, String> map = (HashMap<String, String>) pair.getValue();
                    messageList.add(new Message(map.get("text"), map.get("time"), map.get("sender")));
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Message m = dataSnapshot.getValue(Message.class);
                if (!m.isFromMe()) {
                    messageList.add(m);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Message m = dataSnapshot.getValue(Message.class);
                messageList.add(m);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(mContext, "Failed to load messages.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addChildEventListener(childEventListener);
        mRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void addDummyMessages() {
        add(new Message("Knock knock", Constants.MY_NAME));
        add(new Message("Who's there?", "other"));
        add(new Message("Harry", Constants.MY_NAME));
        add(new Message("Harry who?", "other"));
        add(new Message("Harry up it's cold up here!", Constants.MY_NAME));
    }

    private void add(Message m) {
        messageList.add(m);
        String key = mDatabase.push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = m.toMap();
        childUpdates.put(key, postValues);
        mDatabase.updateChildren(childUpdates);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(messageList.size() - 1);
    }
}
