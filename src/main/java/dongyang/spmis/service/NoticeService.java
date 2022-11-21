package dongyang.spmis.service;

import dongyang.spmis.model.NoticeDTO;

import java.util.ArrayList;

public interface NoticeService {

    public int getNoticeCnt(String user_email);

    public NoticeDTO getNotices(String user_email);

    public boolean noticeCheck(String user_email);

}
