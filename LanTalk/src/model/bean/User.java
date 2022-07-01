package model.bean;

/** 登录用户模型 */
public class User {
    private String account;
    private String password;
    private String name;
    private String department;

    public User() {}

    public User(String account, String password, String name, String department) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.department = department;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
