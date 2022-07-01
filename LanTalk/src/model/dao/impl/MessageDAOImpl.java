package model.dao.impl;

import model.bean.Message;
import model.dao.MessageDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl extends BaseDAO implements MessageDAO {

    public void recordMessage(String name, String department, String target, String message, String time) {
        //SQL查询语句
        String sql = "insert into message(name, department, target, message, time) values(?, ?, ?, ?, ?)";
        try {
            //配置并执行SQL语句
            this.pStatement = this.conn.prepareStatement(sql);
            this.pStatement.setString(1, name);
            this.pStatement.setString(2, department);
            this.pStatement.setString(3, target);
            this.pStatement.setString(4, message);
            this.pStatement.setString(5, time);
            this.pStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public List<Message> queryMessage() {
        List<Message> list = new ArrayList<>();
        //SQL查询语句
        String sql = "select * from message order by time asc";
        try {
            //配置并执行SQL语句
            this.pStatement = this.conn.prepareStatement(sql);
            this.rs = this.pStatement.executeQuery();
            while (this.rs.next()) {
                String name = this.rs.getString("name");
                String department = this.rs.getString("department");
                String target = this.rs.getString("target");
                String msg = this.rs.getString("message");
                String time = this.rs.getString("time");
                Message message = new Message(name, department, target, msg, time);
                list.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return list;
    }

    public boolean deleteMessage() {
        String sql;
        try {
            sql = "select count(*) from message";
            this.pStatement = this.conn.prepareStatement(sql);
            this.rs = this.pStatement.executeQuery();
            if (this.rs.next()) {
                int count = this.rs.getInt(1);
                //若表不为空，则删除数据
                if (count > 0) {
                    sql = "delete from message";
                    this.pStatement = this.conn.prepareStatement(sql);
                    int result = this.pStatement.executeUpdate();
                    return result != 0;
                }
                //若表为空，则无需删除，直接返回true
                else {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return false;
    }
}
