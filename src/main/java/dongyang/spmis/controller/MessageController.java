package dongyang.spmis.controller;

import dongyang.spmis.model.MessageModel.MessageDTO;
import dongyang.spmis.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("message")
    public String messagePage(Model model, Authentication authentication){
        model.addAttribute("messages", messageService.getMessage(authentication.getName()));

        return "message";
    }

    @PostMapping("sendMessage")
    public void sendMessage(HttpServletResponse res, MessageDTO messageDTO, Authentication authentication) throws IOException {
        messageDTO.setSend_user_email(authentication.getName());
        messageService.sendMessage(messageDTO);
        res.sendRedirect("message");
    }

}
