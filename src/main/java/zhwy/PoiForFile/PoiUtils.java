package zhwy.PoiForFile;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PoiUtils {
    private Map<String, Map<String, Object>> orderMap = new HashMap<String, Map<String, Object>>();

    /*把*/
    public static void copy(Object obj, Object dest) {
        // 获取属性
        BeanInfo sourceBean = null;
        try {
            sourceBean = Introspector.getBeanInfo(obj.getClass(), Object.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

        BeanInfo destBean = null;
        try {
            destBean = Introspector.getBeanInfo(dest.getClass(), Object.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
        try {
            for (int i = 0; i < sourceProperty.length; i++) {
                Object value = sourceProperty[i].getReadMethod().invoke(obj);
                if (value != null) {

                    for (int j = 0; j < destProperty.length; j++) {
                        if (sourceProperty[i].getName().equals(destProperty[j].getName()) && sourceProperty[i].getPropertyType() == destProperty[j].getPropertyType()) {
                            // 调用source的getter方法和dest的setter方法
                            destProperty[j].getWriteMethod().invoke(dest, value);
                            break;
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            try {
                throw new Exception("属性复制失败:" + e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 设置一级标题内容及样式
     *
     * @param paragraph
     * @param text
     */
    public  void setLevelTitle1(XWPFDocument document, XWPFParagraph paragraph, String text) {
        /**1.将段落原有文本(原有所有的Run)全部删除*/
        deleteRun(paragraph);
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(text);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.setText("\r");
        createRun.setFontSize(16);
        createRun.setFontFamily("黑体");
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(20);
        paragraph.setSpacingBefore(20);
        addCustomHeadingStyle(document, "标题1", 1.1);
        paragraph.setStyle("标题1");
    }

    /**
     * 设置二级标题内容及样式
     *
     * @param paragraph
     * @param text
     */
    public  void setLevelTitle2(XWPFDocument document, XWPFParagraph paragraph, String text) {
        deleteRun(paragraph);
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(text);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.setText("\r");
        createRun.setFontSize(16);
        createRun.setBold(true);
        createRun.setFontFamily("黑体");
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
        addCustomHeadingStyle(document, "标题2", 1);
        paragraph.setStyle("标题2");
    }

    /**
     * 设置正文文本内容及样式
     *
     * @param paragraph
     * @param text
     */
    public  void setTextPro(XWPFParagraph paragraph, String text) throws Exception{
        deleteRun(paragraph);
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
       // text= URLDecoder.decode(text,"UTF-8");
        createRun.setText(text);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        //separtor.addBreak();
        createRun.addTab();
        createRun.setFontFamily("宋体");
        createRun.setFontSize(12);
        paragraph.setFirstLineIndent(20);
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingBetween(1.5);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
    }

    /**
     * 向段落添加文本
     *
     * @param paragraph
     * @param text
     */
    public  void addTextPro(XWPFParagraph paragraph, String text) {
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.createRun();
        createRun.setText(text);
        XWPFRun separtor = paragraph.createRun();
        /**两段之间添加换行*/
        separtor.addBreak();
        createRun.addTab();
        createRun.setFontFamily("仿宋_GB2312");
        createRun.setFontSize(16);
        paragraph.setFirstLineIndent(20);
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);

        paragraph.addRun(createRun);
        paragraph.addRun(separtor);
    }

    /**
     * 设置表格标题内容及样式
     *
     * @param paragraph
     * @param text
     */
    public  void setTableOrChartTitle(XWPFParagraph paragraph, String text) {
        /**1.将段落原有文本(原有所有的Run)全部删除*/
        deleteRun(paragraph);
        XWPFRun separtor = paragraph.insertNewRun(0);
        /**两段之间添加换行*/
        separtor.setText("\r");
        XWPFRun createRun = paragraph.insertNewRun(1);
        createRun.setText(text);
        createRun.setFontFamily("宋体");
        createRun.setFontSize(12);
        createRun.setBold(true);
        XWPFRun separtor2 = paragraph.insertNewRun(2);
        /**两段之间添加换行*/
        separtor2.setText("\r");
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
    }

    public void deleteRun(XWPFParagraph paragraph) {
        /*1.将段落原有文本(原有所有的Run)全部删除*/
        List<XWPFRun> runs = paragraph.getRuns();
        int runSize = runs.size();
        /*Paragrap中每删除一个run,其所有的run对象就会动态变化，即不能同时遍历和删除*/
        int haveRemoved = 0;
        for (int runIndex = 0; runIndex < runSize; runIndex++) {
            paragraph.removeRun(runIndex - haveRemoved);
            haveRemoved++;
        }
    }

    /**
     * 合并行
     *
     * @param table
     * @param col     需要合并的列
     * @param fromRow 开始行
     * @param toRow   结束行
     */
    public static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                vmerge.setVal(STMerge.RESTART);
            } else {
                vmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setVMerge(vmerge);
            } else {
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setVMerge(vmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    /**
     * 设置表格内容居中
     *
     * @param table
     */
    public void setTableCenter(XWPFTable table) {
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            row.setHeight(400);
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                CTTc cttc = cell.getCTTc();
                CTTcPr ctPr = cttc.addNewTcPr();
                ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
            }
        }
    }

    public void init(XWPFDocument document) {
        //获取段落
        List<XWPFParagraph> paras = document.getParagraphs();

        for (int i = 0; i < paras.size(); i++) {
            XWPFParagraph para = paras.get(i);
//              System.out.println(para.getCTP());//得到xml格式
//              System.out.println(para.getStyleID());//段落级别
//              System.out.println(para.getParagraphText());//段落内容

            String titleLvl = getTitleLvl(document, para);//获取段落级别
            if ("a5".equals(titleLvl) || "HTML".equals(titleLvl) || "".equals(titleLvl) || null == titleLvl) {
                titleLvl = "8";
            }

            if (null != titleLvl && !"".equals(titleLvl) && !"8".equals(titleLvl)) {
                String t = titleLvl;
                String orderCode = getOrderCode(titleLvl);//获取编号
                String text = para.getParagraphText();
               // text = orderCode + " " + text;
                List<XWPFRun> runs = para.getRuns();
                int runSize = runs.size();
                /**Paragrap中每删除一个run,其所有的run对象就会动态变化，即不能同时遍历和删除*/
                int haveRemoved = 0;
                for (int runIndex = 0; runIndex < runSize; runIndex++) {
                    para.removeRun(runIndex - haveRemoved);
                    haveRemoved++;
                }
                if ("1".equals(titleLvl)) {
                    setLevelTitle1(document, para, text);
                }
                if ("2".equals(titleLvl)) {
                    setLevelTitle2(document, para, text);
                }
            }
        }
    }

    /**
     * Word中的大纲级别，可以通过getPPr().getOutlineLvl()直接提取，但需要注意，Word中段落级别，通过如下三种方式定义：
     * 1、直接对段落进行定义；
     * 2、对段落的样式进行定义；
     * 3、对段落样式的基础样式进行定义。
     * 因此，在通过“getPPr().getOutlineLvl()”提取时，需要依次在如上三处读取。
     *
     * @param doc
     * @param para
     * @return
     */
    private String getTitleLvl(XWPFDocument doc, XWPFParagraph para) {
        String titleLvl = "";

        //判断该段落是否设置了大纲级别
        CTP ctp = para.getCTP();
        if (ctp != null) {
            CTPPr pPr = ctp.getPPr();
            if (pPr != null) {
                if (pPr.getOutlineLvl() != null) {
                    return String.valueOf(pPr.getOutlineLvl().getVal());
                }
            }
        }


        //判断该段落的样式是否设置了大纲级别
        if (para.getStyle() != null) {
            if (doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {
                return String.valueOf(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());
            }


            //判断该段落的样式的基础样式是否设置了大纲级别
            if (doc.getStyles().getStyle(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
                    .getCTStyle().getPPr().getOutlineLvl() != null) {
                String styleName = doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
                return String.valueOf(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());
            }
        }
        if (para.getStyleID() != null) {
            return para.getStyleID();
        }
        return titleLvl;
    }

    /**
     * 增加自定义标题样式。这里用的是stackoverflow的源码
     *
     * @param docxDocument 目标文档
     * @param strStyleId   样式名称
     * @param headingLevel 样式级别
     */
    private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, double headingLevel) {


        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf((long) headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    /**
     * 获取标题编号
     *
     * @param titleLvl
     * @return
     */
    private String getOrderCode(String titleLvl) {
        String order = "";

        if ("0".equals(titleLvl) || Integer.parseInt(titleLvl) == 8) {//文档标题||正文
            return "";
        } else if (Integer.parseInt(titleLvl) > 0 && Integer.parseInt(titleLvl) < 8) {//段落标题

            //设置最高级别标题
            Map<String, Object> maxTitleMap = orderMap.get("maxTitleLvlMap");
            if (null == maxTitleMap) {//没有，表示第一次进来
                //最高级别标题赋值
                maxTitleMap = new HashMap<String, Object>();
                maxTitleMap.put("lvl", titleLvl);
                orderMap.put("maxTitleLvlMap", maxTitleMap);
            } else {
                String maxTitleLvl = maxTitleMap.get("lvl") + "";//最上层标题级别(0,1,2,3)
                if (Integer.parseInt(titleLvl) < Integer.parseInt(maxTitleLvl)) {//当前标题级别更高
                    maxTitleMap.put("lvl", titleLvl);//设置最高级别标题
                    orderMap.put("maxTitleLvlMap", maxTitleMap);
                }
            }

            //查父节点标题
            int parentTitleLvl = Integer.parseInt(titleLvl) - 1;//父节点标题级别
            Map<String, Object> cMap = orderMap.get(titleLvl);//当前节点信息
            Map<String, Object> pMap = orderMap.get(parentTitleLvl + "");//父节点信息

            if (0 == parentTitleLvl) {//父节点为文档标题，表明当前节点为1级标题
                int count = 0;
                //最上层标题，没有父节点信息
                if (null == cMap) {//没有当前节点信息
                    cMap = new HashMap<String, Object>();
                } else {
                    count = Integer.parseInt(String.valueOf(cMap.get("cCount")));//当前序个数
                }
                count++;
                order = count + "";
                cMap.put("cOrder", order);//当前序
                cMap.put("cCount", count);//当前序个数
                orderMap.put(titleLvl, cMap);

            } else {//父节点为非文档标题
                int count = 0;
                //如果没有相邻的父节点信息，当前标题级别自动升级
                if (null == pMap) {
                    return getOrderCode(String.valueOf(parentTitleLvl));
                } else {
                    String pOrder = String.valueOf(pMap.get("cOrder"));//父节点序
                    if (null == cMap) {//没有当前节点信息
                        cMap = new HashMap<String, Object>();
                    } else {
                        count = Integer.parseInt(String.valueOf(cMap.get("cCount")));//当前序个数
                    }
                    count++;
                    order = pOrder + "." + count;//当前序编号
                    cMap.put("cOrder", order);//当前序
                    cMap.put("cCount", count);//当前序个数
                    orderMap.put(titleLvl, cMap);
                }
            }

            //字节点标题计数清零
            int childTitleLvl = Integer.parseInt(titleLvl) + 1;//子节点标题级别
            Map<String, Object> cdMap = orderMap.get(childTitleLvl + "");//
            if (null != cdMap) {
                cdMap.put("cCount", 0);//子节点序个数
                orderMap.get(childTitleLvl + "").put("cCount", 0);
            }
        }
        return order;
    }



}
