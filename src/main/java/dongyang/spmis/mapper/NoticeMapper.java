package dongyang.spmis.mapper;

import dongyang.spmis.model.MessageModel.MessageDTO;
import dongyang.spmis.model.NoticeDTO;
import dongyang.spmis.model.Project.NoticeCheck;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

public interface NoticeMapper extends DefaultDBInfo{

    @Select("SELECT check_id, user_email, nc.notice_id, confirm, pn.notice project_name, p.project_id \n" +
            " FROM notice_check nc, project p, project_notice pn " +
            " where pn.notice_id = nc.notice_id and " +
            " p.project_id = pn.project_id and" +
            " user_email = #{user_email}")
    ArrayList<NoticeCheck> findNoticeCheck(String user_email);

    @Select("SELECT * FROM "+ MESSAGE + " WHERE recv_user_email = #{user_email}")
    ArrayList<MessageDTO> findMessage(String user_email);

    @Update("UPDATE " + NOTICE_CHECK + " SET confirm = true WHERE user_email=#{user_email}")
    boolean projectNoticeCheck(String user_email);

    @Update("UPDATE " + MESSAGE + " SET confirm = true WHERE recv_user_email=#{user_email}")
    boolean messageCheck(String user_email);

}
