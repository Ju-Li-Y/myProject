package model.dao.impl;

import model.bean.User;
import model.dao.AccountDAO;

import java.sql.SQLException;

public class AccountDAOImpl extends BaseDAO implements AccountDAO {
    @Override
    public User queryAccount(String account, String password) {
        //SQL查询语句
        String sql = "select * from account where account = ? and password = ?";
        User user = null;
        try {
            //配置并执行SQL语句
            this.pStatement = this.conn.prepareStatement(sql);
            this.pStatement.setString(1, account);
            this.pStatement.setString(2, password);
            this.rs = this.pStatement.executeQuery();
            //如果对应账号存在，则rs.next()方法的值为true
            if (this.rs.next()) {
                String name = this.rs.getString("name");
                String department = this.rs.getString("department");
                user = new User(account, password, name, department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return user;
    }
}
