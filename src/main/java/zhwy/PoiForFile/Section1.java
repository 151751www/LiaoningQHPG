package zhwy.PoiForFile;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

@Service("Section1")
public class Section1 extends BaseSection {


    @Override
    public  void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="基本气候条件";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName)throws  Exception {
        String body ="统计"+stationName+"气象站"+beginTime+"~"+endTime+"年相关气象要素的累年值以及"+beginTime2+"~"+endTime2+"年相关气象要素极值情况如下表。";
        poiUtils.setTextPro(paragraph,body);
    }


    @Override
    public void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) {
        String chartName="";
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
