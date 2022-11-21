package dongyang.spmis.controller;

import dongyang.spmis.mapper.DocumentMapper;
import dongyang.spmis.model.User.UserDTO;
import dongyang.spmis.service.CustomPdfViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class DocumentController {

    HttpSession session;

    @Autowired
    DocumentMapper documentMapper;

    @RequestMapping(value = "/SPMIS_PDF")
    public ModelAndView getPdfView(Model model, @RequestParam("selectproject")String[] selectproject, @RequestParam("selectaction")String[] selectaction,
                                   HttpServletRequest req, HttpServletResponse res) {

        session = req.getSession();
        UserDTO userInfo = (UserDTO) session.getAttribute("mem");

        ModelAndView mav = new ModelAndView();
        mav.setView(new CustomPdfViewService(documentMapper));
        mav.addObject("selectproject", selectproject);
        mav.addObject("selectaction", selectaction);
        mav.addObject("userInfo", userInfo);
        return mav;
    }
}

