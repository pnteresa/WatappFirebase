package id.teresa.watappfirebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by teresa on 9/30/2016.
 */
@IgnoreExtraProperties
public class Message {
    public String text;
    public String time;
    public String sender;

    public Message(String text, Date time, String sender) {
        this.text = text;
        this.time = getFormattedTime(time);
        this.sender = sender;
    }

    public Message(String text, String time, String sender) {
        this.text = text;
        this.time = time;
        this.sender = sender;
    }

    public Message() {
    }

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.time = getFormattedTime(new Date());
    }

    public static String getSdf(Date date) {
        return getSdf(date, Calendar.getInstance().getTime());
    }

    public static String getSdf(Calendar cal1, Calendar cal2) {
        StringBuilder sb = new StringBuilder();
        if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
            sb.append("dd MMM yy hh:mm aaa");
        } else {
            if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                sb.append("hh:mm aaa");
            } else {
                sb.append("dd MMM hh:mm aaa");
            }
        }
        return sb.toString();
    }

    public static String getSdf(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return getSdf(cal1, cal2);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("time", time);
        result.put("sender", sender);

        return result;
    }

    public int getSenderOrdinal() {
        if (sender.equalsIgnoreCase(Constants.MY_NAME)) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean isFromMe() {
        return sender.equalsIgnoreCase(Constants.MY_NAME);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFormattedTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(getSdf(date));
        return sdf.format(date);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
