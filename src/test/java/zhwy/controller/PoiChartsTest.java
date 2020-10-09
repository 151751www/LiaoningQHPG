package zhwy.controller;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


public class PoiChartsTest {




    //预编译正则表达式，加快正则匹配速度


    public static void main(String[] args)  {
        PoiUtils poiUtils =new PoiUtils();
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        XWPFDocument document = null;
        InputStream inputStream=null;

        try {
            File file = ResourceUtils.getFile("F://Liaoning//test2.docx");
            inputStream = new FileInputStream(file);
            document = new XWPFDocument(inputStream);


        Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            String classPath = "zhwy.controller.Section1";

        //处理文字
        while (itPara.hasNext()) {
            XWPFParagraph paragraph = itPara.next();
            String paraText = paragraph.getText();
            //如果没有匹配到指定格式的关键词占位符（如${title}格式的）则不进行后续处理
            if (!pattern.matcher(paraText).find()) {
                continue;
            }
            //提取出文档模板占位符中的章节标题
            String keyInParaText = paraText.split("\\$\\{")[1].split("\\}")[0];
            //如果占位符是大标题
            if ("title".equalsIgnoreCase(keyInParaText)) {
                insertTitle(paragraph);
                continue;
            }
            //如果占位符代表文本总描述
            if ("totalDesc".equalsIgnoreCase(keyInParaText)) {
                insertText(poiUtils,paragraph);
                continue;
            }
            //如果占位符代表章节标题
            if (keyInParaText.contains("section") && keyInParaText.contains("Title")) {
                //获取章节类名
                String name = keyInParaText.substring(0, 8);
                //获取章节类的路径
                //通过类路径获取类对象
                BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                base.replaceSectionTitle(document, paragraph);
                continue;
            }
            //如果占位符代表章节文本描述
            if (keyInParaText.contains("body")) {
                String name = keyInParaText.substring(0, 8);
                BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                base.replaceBody(paragraph);
                continue;
            }
            //如果占位符代表表名
            if (keyInParaText.contains("tableName")) {
                String name = keyInParaText.substring(0, 8);
                BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                base.replaceTableName(paragraph);
                continue;
            }
            //如果占位符代表表
            if (keyInParaText.endsWith("table")) {
                String name = keyInParaText.substring(0, 8);
                BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                base.insertTable(document, paragraph);
                continue;
            }
            //如果占位符代表统计图
            if (keyInParaText.endsWith("chart")) {
                String name = keyInParaText.substring(0, 8);
                paragraph.removeRun(0);
                BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                base.replaceChart(document, keyInParaText);
                continue;
            }
            //如果占位符代表图名
            if (keyInParaText.contains("chartName")) {
                String name = keyInParaText.substring(0, 8);
                BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                base.replaceChartName(paragraph);
                continue;
            }
        }

        //再遍历一次文档,把没有替换的占位符段落删除
        List<IBodyElement> elements = document.getBodyElements();
        int indexTable = 0;
        for (int k = 0; k < elements.size(); k++) {
            IBodyElement bodyElement = elements.get(k);
            //所有段落，如果有${}格式的段落便删除该段落
            if (bodyElement.getElementType().equals(BodyElementType.PARAGRAPH)) {
                XWPFParagraph p = (XWPFParagraph) bodyElement;
                String paraText = p.getText();
                boolean flag = false;
                if (pattern.matcher(paraText).find()) {
                    flag = document.removeBodyElement(k);
                    if (flag) {
                        k--;
                    }
                }
            }
            //如果是表格，那么给表格的前一个段落(即表名加上编号，如表1)
            if (bodyElement.getElementType().equals(BodyElementType.TABLE)) {
                indexTable++;
                XWPFParagraph tableTitleParagraph = (XWPFParagraph) elements.get(k - 1);
                StringBuilder tableTitleText = new StringBuilder(tableTitleParagraph.getParagraphText());
                tableTitleText.insert(0, "表" + indexTable + " ");
                poiUtils.setTableOrChartTitle(tableTitleParagraph, tableTitleText.toString());
            }
        }

        //给章节与小节添加序号
        poiUtils.init(document);

        //导出word文档
        FileOutputStream docxFos = new FileOutputStream("D://test1.docx");
        document.write(docxFos);
        docxFos.flush();
        docxFos.close();
        inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //插入大标题
    public static void insertTitle(XWPFParagraph paragraph) {
        String title = "1. 气候概况";
        List<XWPFRun> runs = paragraph.getRuns();
        int runSize = runs.size();
        /**Paragrap中每删除一个run,其所有的run对象就会动态变化，即不能同时遍历和删除*/
        int haveRemoved = 0;
        for (int runIndex = 0; runIndex < runSize; runIndex++) {
            paragraph.removeRun(runIndex - haveRemoved);
            haveRemoved++;
        }
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(title);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.setText("\r");
        //设置字体大小
        createRun.setFontSize(16);
        //是否加粗
        createRun.setBold(true);
        //设置字体
        createRun.setFontFamily("黑体");
        //设置居中
        paragraph.setAlignment(ParagraphAlignment.LEFT);
    }

    //插入文本描述
    private static void insertText(PoiUtils poiUtils,XWPFParagraph paragraph) {
        String text = "";
        poiUtils.setTextPro(paragraph, text);
    }
}
