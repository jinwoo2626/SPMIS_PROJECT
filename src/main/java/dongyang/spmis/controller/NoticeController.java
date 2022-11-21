package dongyang.spmis.controller;

import dongyang.spmis.model.NoticeDTO;
import dongyang.spmis.model.Project.NoticeCheck;
import dongyang.spmis.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Slf4j
@Controller
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @GetMapping("noticeCnt")
    @ResponseBody
    public int getNoticeCnt(Authentication authentication){
        System.out.println("getName: " + authentication.getName());

        return noticeService.getNoticeCnt(authentication.getName());
    }

    @GetMapping("notice")
    public String getNotice(Model model, Authentication authentication){
        NoticeDTO noticeDTO = noticeService.getNotices(authentication.getName());
        ArrayList<NoticeCheck> nc = noticeDTO.getNoticeChecks();
        for (NoticeCheck noticeCheck : nc) {
            log.info("알림" + noticeCheck.isConfirm());
            System.out.println(noticeCheck.isConfirm());

        }
        model.addAttribute("notices", noticeDTO);
        noticeService.noticeCheck(authentication.getName());
        return "notice";
    }
}
