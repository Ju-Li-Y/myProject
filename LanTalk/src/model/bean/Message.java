package model.bean;

/** 聊天记录模型 */
public class Message {
    private String name;
    private String department;
    private String target;
    private String message;
    private String time;

    public Message() {}

    public Message(String name, String department, String target, String message, String time) {
        this.name = name;
        this.department = department;
        this.target = target;
        this.message = message;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
