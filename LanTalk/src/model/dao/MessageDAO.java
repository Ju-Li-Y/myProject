package model.dao;

import model.bean.Message;

import java.util.List;

public interface MessageDAO {

    /** 记录聊天消息 */
    void recordMessage(String name, String department, String target, String message, String time);

    /** 查询聊天记录 */
    List<Message> queryMessage();

    /** 删除聊天记录 */
    boolean deleteMessage();
}
