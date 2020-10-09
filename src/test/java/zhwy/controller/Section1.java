package zhwy.controller;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.List;

public class Section1 extends BaseSection{

    @Override
    public  void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="水果一";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(XWPFParagraph paragraph) {
        String body ="我藏不住秘密，也藏不住忧伤，正如我藏不住爱你的喜悦，藏不住分离时的彷徨。" +
                "我就是这样坦然，你舍得伤，就伤。如果有一天，你要离开我，我不会留你，我知道你有" +
                "你的理由；如果有一天，你说还爱我，我会告诉你，其实我一直在等你；如果有一天，我们" +
                "擦肩而过，我会停住脚步，凝视你远去的背影，告诉自己那个人我曾经爱过。或许人一生可" +
                "以爱很多次，然而总有一个人，可以让我们笑得最灿烂，哭得最透彻，想得最深切。炊烟起" +
                "了，我在门口等你。夕阳下了，我在山边等你。叶子黄了，我在树下等你。月儿弯了，我在" +
                "十五等你。细雨来了，我在伞下等你。流水冻了，我在河畔等你。生命累了，我在天堂等你" +
                "。我们老了，我在来生等你。";
        poiUtils.setTextPro(paragraph,body);
    }


    @Override
    public void generateChart(XWPFChart chart, String[] series, String[] categories, List<Number[]> values) {
        String chartTitle="香蕉销售数据周统计图";
        List<XDDFChartData> data = chart.getChartSeries();
        XDDFChartData bar = data.get(0);

         int numOfPoints = categories.length;

         String categoryDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 0, 0));

         XDDFDataSource<?> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);
        for (int i = 0; i < values.size(); i++) {
            final String valuesDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, i + 1, i + 1));
            Number[] value = values.get(i);
            final XDDFNumericalDataSource<? extends Number> valuesData = XDDFDataSourcesFactory.fromArray(value, valuesDataRange, i + 1);
            XDDFChartData.Series ser;
            if (i < 3) {
                ser = bar.getSeries().get(i);
                ser.replaceData(categoriesData, valuesData);
            } else {
                ser = bar.addSeries(categoriesData, valuesData);
            }
            CellReference cellReference = chart.setSheetTitle(series[i], 1);
            ser.setTitle(series[i], cellReference);
        }

        chart.plot(bar);
        chart.setTitleText(chartTitle);
        chart.setTitleOverlay(false);
    }

    @Override
    public void replaceChartName(XWPFParagraph paragraph) {
        String chartName="香蕉销售统计图(柱状图)";
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
