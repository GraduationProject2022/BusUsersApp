package hai2022.team.bususersapp.models;

public class Chat {
    private String msg;
    private String date;
    private String time;
    private String userId;

    public Chat() {
    }

    public Chat(String msg, String date, String time, String userId) {
        this.msg = msg;
        this.date = date;
        this.time = time;
        this.userId = userId;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
