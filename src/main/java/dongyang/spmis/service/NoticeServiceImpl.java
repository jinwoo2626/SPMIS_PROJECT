package dongyang.spmis.service;

import dongyang.spmis.mapper.NoticeMapper;
import dongyang.spmis.model.NoticeDTO;
import dongyang.spmis.model.Project.NoticeCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Slf4j
@Service
public class NoticeServiceImpl implements  NoticeService{

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public int getNoticeCnt(String user_email) {

        return 0;
    }

    @Override
    public NoticeDTO getNotices(String user_email) {
        return NoticeDTO.builder()
                .noticeChecks(noticeMapper.findNoticeCheck(user_email))
                .messages(noticeMapper.findMessage(user_email))
                .build();
    }


    @Override
    public boolean noticeCheck(String user_email) {
        System.out.println("notice 체크하기");

        if(noticeMapper.projectNoticeCheck(user_email) && noticeMapper.messageCheck(user_email)){
            return true;
        }
        return false;
    }
}
