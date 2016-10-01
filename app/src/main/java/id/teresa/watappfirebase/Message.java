package id.teresa.watappfirebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by teresa on 9/30/2016.
 */

public class Message {
    private String text;
    private Date time;
    private Sender sender;

    public Message(String text, Date time, Sender sender) {
        this.text = text;
        this.time = time;
        this.sender = sender;
    }


    public Message(String text, Sender sender) {
        this.text = text;
        this.sender = sender;
        this.time = new Date();
    }

    private static String getSdf(Date date) {
        return getSdf(date, Calendar.getInstance().getTime());
    }

    private static String getSdf(Calendar cal1, Calendar cal2) {
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

    private static String getSdf(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return getSdf(cal1, cal2);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(getSdf(time));
        return sdf.format(time);
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public enum Sender {
        ME,
        OTHER
    }
}
