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

        BaseFont baseFont = BaseFont.createFont("src/main/resources/static/assets/fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // 폰트설정

        Font font = new Font(baseFont, 12);
        Font font2 = new Font(baseFont, 16);
        Font font3 = new Font(baseFont, 20);

        ArrayList<ProjectDTO> plist = new ArrayList<>();
        for (int i = 0; i < selectproject.length; i++) {
            plist.add(documentMapper.select2(Integer.parseInt(selectproject[i])));
        }
        ArrayList<ProjectDTO> pfin = new ArrayList<>();
        ArrayList<ProjectDTO> pnfin  =new ArrayList<>();

        //진행중인 프로젝트와 완료한 프로젝트 구분
        for (int i = 0; i < plist.size(); i++){
            if (plist.get(i).getProject_final_date() != null){
                pfin.add(plist.get(i));
            }else {
                pnfin.add(plist.get(i));
            }
        }

        Chunk user = new Chunk(userInfo.getUser_name() + "님의 SPMIS 활동내역", font3);
        Paragraph puser2 = new Paragraph(user);
        puser2.setAlignment(Element.ALIGN_CENTER);
        document.add(puser2);

        //참여한 프로젝트
        Chunk pj = new Chunk("참여한 프로젝트", font2);
        Paragraph pj2 = new Paragraph(pj);
        document.add(pj2);
        document.add(Chunk.NEWLINE);
        Chunk pj3 = new Chunk("진행중인 프로젝트 = " + pnfin.size() + "개", font);
        Paragraph pj4 = new Paragraph(pj3);
        document.add(pj4);
        Chunk pj5 = new Chunk("완료한 프로젝트 = " + pfin.size() + "개", font);
        Paragraph pj6 = new Paragraph(pj5);
        document.add(pj6);
        Chunk pj7 = new Chunk("통합 = " + (pfin.size() + pnfin.size())+ "개", font);
        Paragraph pj8 = new Paragraph(pj7);
        document.add(pj8);
        document.add(Chunk.NEWLINE);

        if (documentDTO.getCheck().isContribute() == true) {
            //기여도 출력
            if (documentDTO.getAvgcontribution() != 0) {
                Chunk ch = new Chunk("기여도", font2);
                Paragraph ch2 = new Paragraph(ch);
                document.add(ch2);
                document.add(Chunk.NEWLINE);

                //선택된 프로젝트가 한개일 때 차트대신 텍스트로 출력
                if (documentDTO.getPjtcontribution().size() == 1) {
                    Chunk con1 = new Chunk("- 프로젝트 기여도 = " + documentDTO.getPjtcontribution().get(0) + "%", font);
                    Paragraph pcon1 = new Paragraph(con1);
                    document.add(pcon1);
                    document.add(Chunk.NEWLINE);

                    //프로젝트별 기여도(작업별)
                    Chunk chunk4 = new Chunk("- 프로젝트 기여도(작업별)", font);
                    Paragraph ph4 = new Paragraph(chunk4);
                    document.add(ph4);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart2 = KanbanContributeChart(documentDTO.getKanbancontribution()); //서비스로부터 차트를 가져옴
                    Image png2 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart2.createBufferedImage(400, 300)));
                    document.add(png2);
                } else { //선택된 프로젝트가 여러개일 때 차트로 출력
                    //평균기여도 출력
                    Chunk chunk = new Chunk("- 평균기여도 = " + documentDTO.getAvgcontribution() + "%", font);
                    Paragraph ph = new Paragraph(chunk);
                    document.add(ph);
                    document.add(Chunk.NEWLINE);

                    //프로젝트별 기여도
                    Chunk chunk2 = new Chunk("- 프로젝트별 기여도", font);
                    Paragraph ph2 = new Paragraph(chunk2);
                    document.add(ph2);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart1 = ContributeChart(documentDTO.getPjtcontribution(), plist); //프로젝트별 기여도 차트
                    Image png1 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart1.createBufferedImage(400, 400)));
                    document.add(png1);

                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);

                    //프로젝트별 기여도(작업별)
                    Chunk chunk4 = new Chunk("- 프로젝트별 기여도(작업별)", font);
                    Paragraph ph4 = new Paragraph(chunk4);
                    document.add(ph4);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart2 = KanbanContributeChart(documentDTO.getKanbancontribution()); //서비스로부터 차트를 가져옴
                    Image png2 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart2.createBufferedImage(400, 300)));
                    document.add(png2);
                }
            } else if (documentDTO.getAvgcontribution() == 0) {
                Chunk ch = new Chunk("기여도", font2);
                Paragraph ch2 = new Paragraph(ch);
                document.add(ch2);
                document.add(Chunk.NEWLINE);

                Chunk elcon = new Chunk("없음", font);
                Paragraph elcon2 = new Paragraph(elcon);
                document.add(elcon2);
                document.add(Chunk.NEWLINE);
            }
        }
        if (documentDTO.getCheck().isLatetask() == true) {
            //활동 지각내역 출력
            if (documentDTO.getAvgtasklate() != 0 && !documentDTO.getTasklatelist().isEmpty() && !documentDTO.getTasklateRatio().isEmpty()) {
                Chunk late = new Chunk("예정 일정 보다 늦어진 작업", font2);
                Paragraph pclate = new Paragraph(late);
                document.add(pclate);
                document.add(Chunk.NEWLINE);

                //선택된 프로젝트가 한개일 때 차트대신 텍스트로 출력
                if (documentDTO.getTasklateRatio().size() == 1) {
                    Chunk late2 = new Chunk("- 작업 지각률 = " + documentDTO.getTasklateRatio().get(0) + "%", font);
                    Paragraph pclate2 = new Paragraph(late2);
                    document.add(pclate2);
                    document.add(Chunk.NEWLINE);
                } else {
                    //평균지각률
                    Chunk late2 = new Chunk("- 평균 작업 지각률 = " + documentDTO.getAvgtasklate() + "%", font);
                    Paragraph pclate2 = new Paragraph(late2);
                    document.add(pclate2);
                    document.add(Chunk.NEWLINE);

                    //프로젝트별 지각률
                    Chunk late3 = new Chunk("- 프로젝트별 작업 지각률", font);
                    Paragraph pclate3 = new Paragraph(late3);
                    document.add(pclate3);
                    document.add(Chunk.NEWLINE);

                    JFreeChart chart3 = LateChart(documentDTO.getTasklateRatio(), plist); //서비스로부터 차트를 가져옴
                    Image png3 = Image.getInstance(
                            ChartUtils.encodeAsPNG(chart3.createBufferedImage(400, 300)));
                    document.add(png3);
                }
                //지각내역 출력
                Chunk late4 = new Chunk("- 작업 지각내역", font);
                Paragraph pclate4 = new Paragraph(late4);
                document.add(pclate4);
                document.add(Chunk.NEWLINE);

                document.add(latetable(documentDTO.getTasklatelist()));

                document.add(Chunk.NEWLINE);
            } else if (documentDTO.getAvgtasklate() == 0 || !documentDTO.getTasklatelist().isEmpty() || !documentDTO.getTasklateRatio().isEmpty()) {
                Chunk late = new Chunk("예정 일정 보다 늦어진 작업", font2);
                Paragraph pclate = new Paragraph(late);
                document.add(pclate);
                document.add(Chunk.NEWLINE);

                Chunk late2 = new Chunk("없음", font);
                Paragraph pclate2 = new Paragraph(late2);
                document.add(pclate2);
                document.add(Chunk.NEWLINE);
            }
        }
        if (documentDTO.getCheck().isTask() == true) {
            //작업내역 출력
            if (!documentDTO.getTasklist().isEmpty()) {
                Chunk task = new Chunk("작업내역", font2);
                Paragraph task1 = new Paragraph(task);
                document.add(task1);
                document.add(Chunk.NEWLINE);

                document.add(tasklisttable(documentDTO.getTasklist()));
                document.add(Chunk.NEWLINE);
            } else {
                Chunk task = new Chunk("작업내역", font2);
                Paragraph task1 = new Paragraph(task);
                document.add(task1);
                document.add(Chunk.NEWLINE);

                Chunk task2 = new Chunk("없음", font);
                Paragraph pctask2 = new Paragraph(task2);
                document.add(pctask2);
                document.add(Chunk.NEWLINE);
            }
        }
    }
    public DocumentDTO documentinfo(UserDTO userInfo, String[] selectproject, String[] selectaction) {
        //나의 활동내역출력
        ArrayList<TaskDTO> tasklist = new ArrayList<>();
        //task설정
        TaskDTO task = new TaskDTO();
        //평균기여도
        int avgcontribution = 0;
        //프로젝트별 기여도
        ArrayList<Integer> pjtcontribution = new ArrayList<>();
        //작업별 기여도
        ArrayList<KanbancontributionDTO> kanbancontribution = new ArrayList<>();
        //지각한 작업
        ArrayList<TaskDTO> tasklatelist = new ArrayList<>();
        //프로젝트별 지각비율
        ArrayList<Integer> tasklateRatio = new ArrayList<>();
        //평균 지각비율
        int avgtasklate = 0;
        //활동내역 체크
        DocumentCheckDTO check = new DocumentCheckDTO();
        check.setTask(false);check.setContribute(false);check.setLatetask(false);

        for (int i = 0; i < selectproject.length; i++) {
            task.setStart_user_email(userInfo.getUser_email());
            task.setProject_id(Integer.parseInt(selectproject[i]));
            for (int j = 0; j < selectaction.length; j++) {
                if (selectaction[j].equals("1")) {  //나의 기여도 출력하기
                    check.setContribute(true);
                    //평균기여도//프로젝트별 기여도
                    pjtcontribution.add((int) ((documentMapper.cntusertask(task)/documentMapper.cntwholetask(task))*100));
                    //작업별기여도
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

                } else if (selectaction[j].equals("2")) { //나의 활동지각내역 출력하기
                    check.setLatetask(true);
                    int cnt = 0;
                    ArrayList<TaskDTO> usertasklist = new ArrayList<>();
                    usertasklist.addAll(documentMapper.documentinfo(task));
                    for(int k = 0; k < usertasklist.size(); k++){
                        if (usertasklist.get(k).getFinal_date() != null) {
                            Date date1 = usertasklist.get(k).getFinal_date();
                            Date date2 = usertasklist.get(k).getFinal_expect_date();

                            if (date1.after(date2)) {  //지각이면 TRUE / 지각이 아니면 FALSE
                                tasklatelist.add(usertasklist.get(k));
                                cnt += 1;
                            }
                        }
                    }
                    tasklateRatio.add((int) ((cnt / (double)usertasklist.size())*100));
                    cnt = 0;
                }
                else if (selectaction[j].equals("3")) {  //나의 활동내역 출력하기
                    check.setTask(true);
                    tasklist.addAll(documentMapper.documentinfo(task));
                }
            }
        }
        //평균기여도
        double sum = 0;
        for(int x : pjtcontribution){
            sum += x;
        }
        avgcontribution = (int) (sum / pjtcontribution.size());

        //평균작업지각률
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

        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); //데이터셋 생성
        for (int i = 0; i < pjtcontribution.size(); i++) {
            dataset.setValue(pjtcontribution.get(i), "프로젝트", plist.get(i).getProject_name()); //데이터셋에 데이터 넣기
            //dataset.setValue(value, rowKey, columnKey); 형식으로 들어간다.
        }
        JFreeChart chart = null; //차트 객체 선언
        String title = ""; //타이틀의 제목
        try {
            //막대 그래프
            chart = ChartFactory.createBarChart( //세로형식의 막대그래프를 만듦
                    title,
                    "",
                    "기여도(%)",
                    dataset,
                    PlotOrientation.VERTICAL, //세로로 차트를 만든다는 의미
                    true, true, false);

//            //제목, 타이틀의 폰트와 글씨크기를 설정
            chart.getTitle().setFont(new java.awt.Font("./src/main/resources/static/assets/fonts/malgun.ttf",Font.BOLD, 15));

            //범례, 범례의 폰트와 글씨크기를 설정
            chart.getLegend().setItemFont(
                    new java.awt.Font("./src/main/resources/static/assets/fonts/malgun.ttf",Font.NORMAL, 10));


            java.awt.Font font = new java.awt.Font("./src/main/resources/static/assets/fonts/malgun.ttf", Font.BOLD,12);
            Color color = new Color(0,0,0);
            StandardChartTheme chartTheme =
                    (StandardChartTheme) StandardChartTheme.createJFreeTheme(); //테마 설정

            chartTheme.setExtraLargeFont(font); //폰트 크기에 따라 테마를 다르게 설정
            chartTheme.setLargeFont(font);
            chartTheme.setRegularFont(font);
            chartTheme.setSmallFont(font);

            chartTheme.setAxisLabelPaint(color); //축, 범례등의 색상을 변경
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
                    "기여도(%)",
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
            dataset.setValue(tasklateRatio.get(i), "프로젝트", plist.get(i).getProject_name());
        }
        JFreeChart chart = null;
        String title = "";
        try {
            //막대 그래프
            chart = ChartFactory.createBarChart(
                    title,
                    "",
                    "지각률(%)",
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

        //폰트, 날짜포맷 등.. 기본설정
        BaseFont baseFont = BaseFont.createFont("src/main/resources/static/assets/fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        SimpleDateFormat time = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");

        //테이블
        PdfPTable pllate = new PdfPTable(4);

        PdfPCell pllate1 = new PdfPCell(new Phrase("프로젝트 명", font));
        pllate1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pllate2 = new PdfPCell(new Phrase("작업명", font));
        pllate2.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pllate3 = new PdfPCell(new Phrase("작업종료예정시각", font));
        pllate3.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pllate4 = new PdfPCell(new Phrase("작업종료시각", font));
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

        //폰트, 날짜포맷 등.. 기본설정
        BaseFont baseFont = BaseFont.createFont("src/main/resources/static/assets/fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        SimpleDateFormat time = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");

        //테이블
        PdfPTable ptask = new PdfPTable(3);

        PdfPCell ptask1 = new PdfPCell(new Phrase("프로젝트 명", font));
        ptask1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell ptask2 = new PdfPCell(new Phrase("작업명", font));
        ptask2.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell ptask3 = new PdfPCell(new Phrase("작업종료시각", font));
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
