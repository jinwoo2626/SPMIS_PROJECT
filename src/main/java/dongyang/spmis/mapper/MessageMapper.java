package dongyang.spmis.mapper;

import dongyang.spmis.model.MessageModel.MessageDTO;
import dongyang.spmis.service.MessageService;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

public interface MessageMapper extends DefaultDBInfo {

    @Select("SELECT * FROM " + MESSAGE + " WHERE send_user_email = #{user_email}")
    public ArrayList<MessageDTO> findMessageBySendUserEmail(String user_email);

    @Select("SELECT * FROM " + MESSAGE + " WHERE recv_user_email = #{user_email} ")
    public ArrayList<MessageDTO> findMessageByRecvUserEmail(String user_email);

    @Select("SELECT * FROM " + MESSAGE + " WHERE recv_user_email = #{user_email} or send_user_email = #{user_email}")
    public ArrayList<MessageDTO> findMessageByUserEmail(String user_email);

    @Insert("INSERT INTO  " + MESSAGE + " WHERE VALUES(null, #{send_user_email}, #{recv_user_email}, #{message}, false)")
    public boolean saveMessage(MessageDTO message);
}


