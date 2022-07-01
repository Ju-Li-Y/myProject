package model.dao;

import model.bean.User;

public interface AccountDAO {
    /**
     * 查询account表，验证账号并返回登录用户类型
     * @return User
     */
    User queryAccount(String account, String password);

}
