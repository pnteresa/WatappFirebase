package id.teresa.watappfirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.Query;

import java.util.List;

/**
 * Created by teresa on 9/30/2016.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public Context mContext;
    private List<Message> mList;


    public MessageAdapter(Context mContext, List<Message> myDataset) {
        this.mContext = mContext;
        mList = myDataset;
    }

    public MessageAdapter(Query query, List<Message> myDataset) {
        this.mContext = mContext;
        mList = myDataset;
    }

    public void remove(Message item) {
        int position = mList.indexOf(item);
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v;
        if (viewType == 0) { //0: me
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_me, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_you, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mList.get(position);
        return message.getSenderOrdinal();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message m = mList.get(position);
        if (holder.mText != null && holder.mTime != null) {
            holder.mText.setText(m.getText());
            holder.mTime.setText(m.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public TextView mTime;

        public ViewHolder(View v) {
            super(v);
            mText = (TextView) v.findViewById(R.id.text);
            mTime = (TextView) v.findViewById(R.id.time);
        }

        public void add(int position, Message item) {
            mList.add(position, item);
            notifyItemInserted(position);
        }
    }
}