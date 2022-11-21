package dongyang.spmis.controller;

import dongyang.spmis.model.Dashboard.DashboardTaskDTO;
import dongyang.spmis.model.Project.DiscordLinkDTO;
import dongyang.spmis.model.Project.ProjectDTO;
import dongyang.spmis.model.Project.ProjectJoinDiscordDTO;
import dongyang.spmis.model.User.UserDTO;
import dongyang.spmis.service.ProjectService;
import dongyang.spmis.service.WebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class DashboardController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private WebHookService webhookService;

    HttpSession session;

    @GetMapping("dashboard")
    public String dashboard(Model model, HttpServletRequest req, HttpServletResponse res) {
        session = req.getSession();
        UserDTO userInfo = (UserDTO) session.getAttribute("mem");

        ArrayList<ProjectDTO> projectList = projectService.selectProjects(userInfo);
        ArrayList<DashboardTaskDTO> tasks = projectService.selectDashboardTask(userInfo.getUser_email());
        ArrayList<ProjectJoinDiscordDTO> links = projectService.FindDiscordLinkByUserEmail(userInfo.getUser_email());

        System.out.println(projectList);

        model.addAttribute("projectList", projectList);
        model.addAttribute("tasks", tasks);
        model.addAttribute("links", links);


        return "dashboard";
    }
}
