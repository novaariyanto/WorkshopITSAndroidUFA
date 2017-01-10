package id.sch.smktiufa.workshopitsandroid.job;

import java.util.Objects;

/**
 * Created by smktiufa on 17/12/16.
 */

public class EventMessage {

    public static int ADD = 100;
    public static int SUCCES = 200;
    public static int ERROR = 400;

    private int id;
    private int status;
    private Object content;


    public EventMessage(int id,int status,Object content) {

        this.content = content;
        this.status = status;
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public Object getContent() {
        return content;
    }

}
