package dongyang.spmis.service;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dongyang.spmis.mapper.DocumentMapper;
import dongyang.spmis.model.Document.DocumentCheckDTO;
import dongyang.spmis.model.Document.DocumentDTO;
import dongyang.spmis.model.Document.KanbancontributionDTO;
import dongyang.spmis.model.Project.ProjectDTO;
import dongyang.spmis.model.Task.KanbanDTO;
import dongyang.spmis.model.Task.TaskDTO;
import dongyang.spmis.model.User.UserDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class CustomPdfViewService extends AbstractPdfView{

    public CustomPdfViewService(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }
    private DocumentMapper documentMapper;
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] selectproject = (String[]) model.get("selectproject");
        String[] selectaction = (String[]) model.get("selectaction");
        UserDTO userInfo = new UserDTO();
        userInfo = (UserDTO) model.get("userInfo");

        DocumentDTO documentDTO = documentinfo(userInfo, selectproject, selectaction);

        BaseFont baseFont = BaseFont.createFont("src/main/resources/static/assets/fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // ????????????

        Font font = new Font(baseFont, 12);
        Font font2 = new Font(baseFont, 16);
        Font font3 = new Font(baseFont, 20);

        ArrayList<ProjectDTO> plist = new ArrayList<>();
        for (int i = 0; i < selectproject.length; i++) {
            plist.add(documentMapper.select2(Integer.parseInt(selectproject[i])));
        }
        ArrayList<ProjectDTO> pfin = new ArrayList<>();
        ArrayList<ProjectDTO> pnfin  =new ArrayList<>();

        //???????????? ??????????????? ????????? ???????????? ??????
        for (int i = 0; i < plist.size(); i++){
            if (plist.get(i).getProject_final_date() != null){
                pfin.add(plist.get(i));
            }else {
                pnfin.add(plist.get(i));
            }
        }

        Chunk user = new Chunk(userInfo.getUser_name() + "?????? SPMIS ????????????", font3);
        Paragraph puser2 = new Paragraph(user);
        puser2.setAlignment(Element.ALIGN_CENTER);
        document.add(puser2);

        //????????? ????????????
        Chunk pj = new Chunk("????????? ????????????", font2);
        Paragraph pj2 = new Paragraph(pj);
        document.add(pj2);
        document.add(Chunk.NEWLINE);
        Chunk pj3 = new Chunk("???????????? ???????????? = " + pnfin.size() + "???", font);
        Paragraph pj4 = new Paragraph(pj3);
        document.add(pj4);
        Chunk pj5 = new Chunk("????????? ???????????? = " + pfin.size() + "???", font);
        Paragraph pj6 = new Paragraph(pj5);
        document.add(pj6);
        Chunk pj7 = new Chunk("?????? = " + (pfin.size() + pnfin.size())+ "???", font);
        Paragraph pj8 = new Paragraph(pj7);
        document.add(pj8);
        document.add(Chunk.NEWLINE);

        if (documentDTO.getCheck().isContribute() == true) {
            //????????? ??????
            if (documentDTO.getAvgcontribution() != 0) {
                Chunk ch = new Chunk("?????????", font2);
                Paragraph ch2 = new Paragraph(ch);
                document.add(ch2);
                document.add(Chunk.NEWLINE);

                //????????? ??????????????? ????????? ??? ???????????? ???????????? ??????
                if (documentDTO.getPjtcontribution().size() == 1) {
                    Chunk con1 = new Chunk("- ???????????? ????????? = " + documentDTO.getPjtcontribution().get(0) + "%", font);
                    Paragraph pcon1 = new Paragraph(con1);
                    document.add(pcon1);
                    document.add(Chunk.NEWLINE);

                    //??????????????? ?????????(?????????)
                    Chunk chunk4 = new Chunk("- ???????????? ?????????(?????????)", font);
                    Paragraph ph4 = new Paragraph(chunk4);
                    document.add(ph4);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart2 = KanbanContributeChart(documentDTO.getKanbancontribution()); //?????????????????? ????????? ?????????
                    Image png2 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart2.createBufferedImage(400, 300)));
                    document.add(png2);
                } else { //????????? ??????????????? ???????????? ??? ????????? ??????
                    //??????????????? ??????
                    Chunk chunk = new Chunk("- ??????????????? = " + documentDTO.getAvgcontribution() + "%", font);
                    Paragraph ph = new Paragraph(chunk);
                    document.add(ph);
                    document.add(Chunk.NEWLINE);

                    //??????????????? ?????????
                    Chunk chunk2 = new Chunk("- ??????????????? ?????????", font);
                    Paragraph ph2 = new Paragraph(chunk2);
                    document.add(ph2);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart1 = ContributeChart(documentDTO.getPjtcontribution(), plist); //??????????????? ????????? ??????
                    Image png1 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart1.createBufferedImage(400, 400)));
                    document.add(png1);

                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);

                    //??????????????? ?????????(?????????)
                    Chunk chunk4 = new Chunk("- ??????????????? ?????????(?????????)", font);
                    Paragraph ph4 = new Paragraph(chunk4);
                    document.add(ph4);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart2 = KanbanContributeChart(documentDTO.getKanbancontribution()); //?????????????????? ????????? ?????????
                    Image png2 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart2.createBufferedImage(400, 300)));
                    document.add(png2);
                }
            } else if (documentDTO.getAvgcontribution() == 0) {
                Chunk ch = new Chunk("?????????", font2);
                Paragraph ch2 = new Paragraph(ch);
                document.add(ch2);
                document.add(Chunk.NEWLINE);

                Chunk elcon = new Chunk("??????", font);
                Paragraph elcon2 = new Paragraph(elcon);
                document.add(elcon2);
                document.add(Chunk.NEWLINE);
            }
        }
        if (documentDTO.getCheck().isLatetask() == true) {
            //?????? ???????????? ??????
            if (documentDTO.getAvgtasklate() != 0 && !documentDTO.getTasklatelist().isEmpty() && !documentDTO.getTasklateRatio().isEmpty()) {
                Chunk late = new Chunk("?????? ?????? ?????? ????????? ??????", font2);
                Paragraph pclate = new Paragraph(late);
                document.add(pclate);
                document.add(Chunk.NEWLINE);

                //????????? ??????????????? ????????? ??? ???????????? ???????????? ??????
                if (documentDTO.getTasklateRatio().size() == 1) {
                    Chunk late2 = new Chunk("- ?????? ????????? = " + documentDTO.getTasklateRatio().get(0) + "%", font);
                    Paragraph pclate2 = new Paragraph(late2);
                    document.add(pclate2);
                    document.add(Chunk.NEWLINE);
                } else {
                    //???????????????
                    Chunk late2 = new Chunk("- ?????? ?????? ????????? = " + documentDTO.getAvgtasklate() + "%", font);
                    Paragraph pclate2 = new Paragraph(late2);
                    document.add(pclate2);
                    document.add(Chunk.NEWLINE);

                    //??????????????? ?????????
                    Chunk late3 = new Chunk("- ??????????????? ?????? ?????????", font);
                    Paragraph pclate3 = new Paragraph(late3);
                    document.add(pclate3);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart3 = LateChart(documentDTO.getTasklateRatio(), plist); //?????????????????? ????????? ?????????
                    Image png3 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart3.createBufferedImage(400, 300)));
                    document.add(png3);
                }
                //???????????? ??????
                Chunk late4 = new Chunk("- ?????? ????????????", font);
                Paragraph pclate4 = new Paragraph(late4);
                document.add(pclate4);
                document.add(Chunk.NEWLINE);

                document.add(latetable(documentDTO.getTasklatelist()));

                document.add(Chunk.NEWLINE);
            } else if (documentDTO.getAvgtasklate() == 0 || !documentDTO.getTasklatelist().isEmpty() || !documentDTO.getTasklateRatio().isEmpty()) {
                Chunk late = new Chunk("?????? ?????? ?????? ????????? ??????", font2);
                Paragraph pclate = new Paragraph(late);
                document.add(pclate);
                document.add(Chunk.NEWLINE);

                Chunk late2 = new Chunk("??????", font);
                Paragraph pclate2 = new Paragraph(late2);
                document.add(pclate2);
                document.add(Chunk.NEWLINE);
            }
        }
        if (documentDTO.getCheck().isTask() == true) {
            //???????????? ??????
            if (!documentDTO.getTasklist().isEmpty()) {
                Chunk task = new Chunk("????????????", font2);
                Paragraph task1 = new Paragraph(task);
                document.add(task1);
                document.add(Chunk.NEWLINE);

                document.add(tasklisttable(documentDTO.getTasklist()));
                document.add(Chunk.NEWLINE);
            } else {
                Chunk task = new Chunk("????????????", font2);
                Paragraph task1 = new Paragraph(task);
                document.add(task1);
                document.add(Chunk.NEWLINE);

                Chunk task2 = new Chunk("??????", font);
                Paragraph pctask2 = new Paragraph(task2);
                document.add(pctask2);
                document.add(Chunk.NEWLINE);
            }
        }
    }
    public DocumentDTO documentinfo(UserDTO userInfo, String[] selectproject, String[] selectaction) {
        //?????? ??????????????????
        ArrayList<TaskDTO> tasklist = new ArrayList<>();
        //task??????
        TaskDTO task = new TaskDTO();
        //???????????????
        int avgcontribution = 0;
        //??????????????? ?????????
        ArrayList<Integer> pjtcontribution = new ArrayList<>();
        //????????? ?????????
        ArrayList<KanbancontributionDTO> kanbancontribution = new ArrayList<>();
        //????????? ??????
        ArrayList<TaskDTO> tasklatelist = new ArrayList<>();
        //??????????????? ????????????
        ArrayList<Integer> tasklateRatio = new ArrayList<>();
        //?????? ????????????
        int avgtasklate = 0;
        //???????????? ??????
        DocumentCheckDTO check = new DocumentCheckDTO();
        check.setTask(false);check.setContribute(false);check.setLatetask(false);

        for (int i = 0; i < selectproject.length; i++) {
            task.setStart_user_email(userInfo.getUser_email());
            task.setProject_id(Integer.parseInt(selectproject[i]));
            for (int j = 0; j < selectaction.length; j++) {
                if (selectaction[j].equals("1")) {  //?????? ????????? ????????????
                    check.setContribute(true);
                    //???????????????//??????????????? ?????????
                    pjtcontribution.add((int) ((documentMapper.cntusertask(task)/documentMapper.cntwholetask(task))*100));
                    //??????????????????
                    for(KanbanDTO x : documentMapper.getkanbanid(Integer.parseInt(selectproject[i]))){
                        if(x.getKanban_status().equals("TO DO")|| x.getKanban_status().equals("DOING") || x.getKanban_status().equals("DONE")) {
                            KanbancontributionDTO cntkanban = new KanbancontributionDTO();
                            task.setKanban_id(x.getKanban_id());
                            cntkanban.setContribution((int) ((documentMapper.cntusertaskkanban(task) / documentMapper.cntwholetaskkanban(task)) * 100));
                            cntkanban.setProject_name(documentMapper.getproject_name(selectproject[i]));
                            cntkanban.setKanban_status(x.getKanban_status());
                            kanbancontribution.add(cntkanban);
                        }else {
                            continue;
                        }
                    }

                } else if (selectaction[j].equals("2")) { //?????? ?????????????????? ????????????
                    check.setLatetask(true);
                    int cnt = 0;
                    ArrayList<TaskDTO> usertasklist = new ArrayList<>();
                    usertasklist.addAll(documentMapper.documentinfo(task));
                    for(int k = 0; k < usertasklist.size(); k++){
                        if (usertasklist.get(k).getFinal_date() != null) {
                            Date date1 = usertasklist.get(k).getFinal_date();
                            Date date2 = usertasklist.get(k).getFinal_expect_date();

                            if (date1.after(date2)) {  //???????????? TRUE / ????????? ????????? FALSE
                                tasklatelist.add(usertasklist.get(k));
                                cnt += 1;
                            }
                        }
                    }
                    tasklateRatio.add((int) ((cnt / (double)usertasklist.size())*100));
                    cnt = 0;
                }
                else if (selectaction[j].equals("3")) {  //?????? ???????????? ????????????
                    check.setTask(true);
                    tasklist.addAll(documentMapper.documentinfo(task));
                }
            }
        }
        //???????????????
        double sum = 0;
        for(int x : pjtcontribution){
            sum += x;
        }
        avgcontribution = (int) (sum / pjtcontribution.size());

        //?????????????????????
        double sum2 = 0;
        for(int y : tasklateRatio){
            sum2 += y;
        }
        avgtasklate = (int) (sum2 / tasklateRatio.size());

        return DocumentDTO.builder()
                .tasklist(tasklist)
                .task(task)
                .avgcontribution(avgcontribution)
                .pjtcontribution(pjtcontribution)
                .kanbancontribution(kanbancontribution)
                .tasklatelist(tasklatelist)
                .tasklateRatio(tasklateRatio)
                .avgtasklate(avgtasklate)
                .check(check)
                .build();
    }
    public JFreeChart ContributeChart(ArrayList<Integer> pjtcontribution, ArrayList<ProjectDTO> plist) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); //???????????? ??????
        for (int i = 0; i < pjtcontribution.size(); i++) {
            dataset.setValue(pjtcontribution.get(i), "????????????", plist.get(i).getProject_name()); //??????????????? ????????? ??????
            //dataset.setValue(value, rowKey, columnKey); ???????????? ????????????.
        }
        JFreeChart chart = null; //?????? ?????? ??????
        String title = ""; //???????????? ??????
        try {
            //?????? ?????????
            chart = ChartFactory.createBarChart( //??????????????? ?????????????????? ??????
                    title,
                    "",
                    "?????????(%)",
                    dataset,
                    PlotOrientation.VERTICAL, //????????? ????????? ???????????? ??????
                    true, true, false);

//            //??????, ???????????? ????????? ??????????????? ??????
            chart.getTitle().setFont(new java.awt.Font("./src/main/resources/static/assets/fonts/malgun.ttf",Font.BOLD, 15));

            //??????, ????????? ????????? ??????????????? ??????
            chart.getLegend().setItemFont(
                    new java.awt.Font("./src/main/resources/static/assets/fonts/malgun.ttf",Font.NORMAL, 10));


            java.awt.Font font = new java.awt.Font("./src/main/resources/static/assets/fonts/malgun.ttf", Font.BOLD,12);
            Color color = new Color(0,0,0);
            StandardChartTheme chartTheme =
                    (StandardChartTheme) StandardChartTheme.createJFreeTheme(); //?????? ??????

            chartTheme.setExtraLargeFont(font); //?????? ????????? ?????? ????????? ????????? ??????
            chartTheme.setLargeFont(font);
            chartTheme.setRegularFont(font);
            chartTheme.setSmallFont(font);

            chartTheme.setAxisLabelPaint(color); //???, ???????????? ????????? ??????
            chartTheme.setLegendItemPaint(color);
            chartTheme.setItemLabelPaint(color);
            chartTheme.apply(chart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return chart;
    }

    public JFreeChart KanbanContributeChart(ArrayList<KanbancontributionDTO> kanbancontribution) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < kanbancontribution.size(); i++) {
            dataset.setValue(kanbancontribution.get(i).getContribution(), kanbancontribution.get(i).getKanban_status(), kanbancontribution.get(i).getProject_name());
        }
        JFreeChart chart = null;
        String title = "";
        try {
            chart = ChartFactory.createBarChart(
                    title,
                    "",
                    "?????????(%)",
                    dataset,
                    PlotOrientation.VERTICAL,true, true, false);

            chart.getTitle().setFont(new java.awt.Font("src/main/resources/static/assets/fonts/malgun.ttf", Font.BOLD, 15));

            chart.getLegend().setItemFont(
                    new java.awt.Font("src/main/resources/static/assets/fonts/malgun.ttf", Font.NORMAL, 10));

            java.awt.Font font = new java.awt.Font("src/main/resources/static/assets/fonts/malgun.ttf", Font.BOLD, 12);
            Color color = new Color(0, 0, 0);
            StandardChartTheme chartTheme =
                    (StandardChartTheme) StandardChartTheme.createJFreeTheme();

            chartTheme.setExtraLargeFont(font);
            chartTheme.setLargeFont(font);
            chartTheme.setRegularFont(font);
            chartTheme.setSmallFont(font);

            chartTheme.setAxisLabelPaint(color);
            chartTheme.setLegendItemPaint(color);
            chartTheme.setItemLabelPaint(color);
            chartTheme.apply(chart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return chart;
    }
    public JFreeChart LateChart(ArrayList<Integer> tasklateRatio, ArrayList<ProjectDTO> plist) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < tasklateRatio.size(); i++) {
            dataset.setValue(tasklateRatio.get(i), "????????????", plist.get(i).getProject_name());
        }
        JFreeChart chart = null;
        String title = "";
        try {
            //?????? ?????????
            chart = ChartFactory.createBarChart(
                    title,
                    "",
                    "?????????(%)",
                    dataset,
                    PlotOrientation.VERTICAL,true, true, false);

            chart.getTitle().setFont(new java.awt.Font("src/main/resources/static/assets/fonts/malgun.ttf",Font.BOLD, 15));

            chart.getLegend().setItemFont(
                    new java.awt.Font("src/main/resources/static/assets/fonts/malgun.ttf",Font.NORMAL, 10));

            java.awt.Font font = new java.awt.Font("src/main/resources/static/assets/fonts/malgun.ttf", Font.BOLD,12);
            Color color = new Color(0,0,0);
            StandardChartTheme chartTheme =
                    (StandardChartTheme) StandardChartTheme.createJFreeTheme();

            chartTheme.setExtraLargeFont(font);
            chartTheme.setLargeFont(font);
            chartTheme.setRegularFont(font);
            chartTheme.setSmallFont(font);

            chartTheme.setAxisLabelPaint(color);
            chartTheme.setLegendItemPaint(color);
            chartTheme.setItemLabelPaint(color);
            chartTheme.apply(chart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return chart;
    }
    public PdfPTable latetable(ArrayList<TaskDTO> tasklatelist) throws DocumentException, IOException {

        //??????, ???????????? ???.. ????????????
        BaseFont baseFont = BaseFont.createFont("src/main/resources/static/assets/fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        SimpleDateFormat time = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");

        //?????????
        PdfPTable pllate = new PdfPTable(4);

        PdfPCell pllate1 = new PdfPCell(new Phrase("???????????? ???", font));
        pllate1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pllate2 = new PdfPCell(new Phrase("?????????", font));
        pllate2.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pllate3 = new PdfPCell(new Phrase("????????????????????????", font));
        pllate3.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pllate4 = new PdfPCell(new Phrase("??????????????????", font));
        pllate4.setHorizontalAlignment(Element.ALIGN_CENTER);

        pllate.addCell(pllate1);
        pllate.addCell(pllate2);
        pllate.addCell(pllate3);
        pllate.addCell(pllate4);

        for (int i = 0; i < tasklatelist.size(); i++) {
            PdfPCell pllatec1 = new PdfPCell(new Phrase(documentMapper.getproject_name(String.valueOf(tasklatelist.get(i).getProject_id())), font));
            pllatec1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell pllatec2 = new PdfPCell(new Phrase(tasklatelist.get(i).getTask_subject(), font));
            pllatec2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell pllatec3 = new PdfPCell(new Phrase(time.format(tasklatelist.get(i).getFinal_expect_date()), font));
            PdfPCell pllatec4 = new PdfPCell(new Phrase(time.format(tasklatelist.get(i).getFinal_date()), font));

            pllate.addCell(pllatec1);
            pllate.addCell(pllatec2);
            pllate.addCell(pllatec3);
            pllate.addCell(pllatec4);
        }
        return pllate;
    }
    public PdfPTable tasklisttable(ArrayList<TaskDTO> tasklist) throws DocumentException, IOException {

        //??????, ???????????? ???.. ????????????
        BaseFont baseFont = BaseFont.createFont("src/main/resources/static/assets/fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        SimpleDateFormat time = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");

        //?????????
        PdfPTable ptask = new PdfPTable(3);

        PdfPCell ptask1 = new PdfPCell(new Phrase("???????????? ???", font));
        ptask1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell ptask2 = new PdfPCell(new Phrase("?????????", font));
        ptask2.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell ptask3 = new PdfPCell(new Phrase("??????????????????", font));
        ptask3.setHorizontalAlignment(Element.ALIGN_CENTER);

        ptask.addCell(ptask1);
        ptask.addCell(ptask2);
        ptask.addCell(ptask3);

        for (int i = 0; i < tasklist.size(); i++){
            if(tasklist.get(i).getFinal_date() != null) {
                PdfPCell ptaskc1 = new PdfPCell(new Phrase(documentMapper.getproject_name(String.valueOf(tasklist.get(i).getProject_id())), font));
                ptaskc1.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ptaskc2 = new PdfPCell(new Phrase(tasklist.get(i).getTask_subject(), font));
                ptaskc2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ptaskc3 = new PdfPCell(new Phrase(time.format(tasklist.get(i).getFinal_date()), font));

                ptask.addCell(ptaskc1);
                ptask.addCell(ptaskc2);
                ptask.addCell(ptaskc3);
            }else {
                PdfPCell ptaskc1 = new PdfPCell(new Phrase(documentMapper.getproject_name(String.valueOf(tasklist.get(i).getProject_id())), font));
                ptaskc1.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ptaskc2 = new PdfPCell(new Phrase(tasklist.get(i).getTask_subject(), font));
                ptaskc2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell ptaskc3 = new PdfPCell(new Phrase(" ", font));
                ptaskc3.setHorizontalAlignment(Element.ALIGN_CENTER);

                ptask.addCell(ptaskc1);
                ptask.addCell(ptaskc2);
                ptask.addCell(ptaskc3);
            }
        }
        return ptask;
    }

}
