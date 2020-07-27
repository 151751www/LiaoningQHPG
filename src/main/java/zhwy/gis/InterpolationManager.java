package zhwy.gis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InterpolationManager {
	private double _beginLon = 0;
	private double _beginLat = 0;
	private double _size = 0;

	// / <summary>
	// / 构造函数
	// / </summary>
	// / <param name="beginLon">开始经度，网格左下角</param>
	// / <param name="beginLat">开始纬度，网格左下角</param>
	// / <param name="size">分辨率，设0.01为1公里</param>
	public InterpolationManager(double beginLon, double beginLat, double size) {
		_beginLon = beginLon;
		_beginLat = beginLat;
		_size = size;
	}

	// / <summary>
	// / 反距离权重插值
	// / </summary>
	// / <param name="listGisPoint">待插值点集合</param>
	// / <param name="rowCount">插值结果网格行数</param>
	// / <param name="columnCount">插值结果网格列数</param>
	// / <param name="nearPointCount">做权重分析的最近点数</param>
	// / <returns></returns>
	public double[][] IDW(List<GisPoint> listGisPoint, int rowCount,
			int columnCount, int nearPointCount) {
		if (listGisPoint.size() <= 0) {
            return null;
        }

		double[] arrXY = null;
		for (GisPoint point : listGisPoint) {
			arrXY = ConvertToRowCol(point.Lon, point.Lat);
			point.ColIndex = arrXY[0];
			point.RowIndex = arrXY[1];
		}
		int b = listGisPoint.size();
		double[][] arrValue = new double[rowCount][columnCount];
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)// 纵轴
		{
			for (int colIndex = 0; colIndex < columnCount; colIndex++)// 横轴
			{
				double valueTop = 0;
				double valueBottom = 0;

				List<GisPoint> listNearPoint = GetNearPoint(listGisPoint,
						rowIndex, colIndex, nearPointCount);
				int a = listNearPoint.size();
				for (int k = 0; k < listNearPoint.size(); k++) {
					// 计算距离，这里取P为2，如果调整，需要开方后在幂运算
					double p = 2;
					double distance = Math.pow(
							Math.pow(listNearPoint.get(k).ColIndex - colIndex,
									2)
									+ Math.pow(listNearPoint.get(k).RowIndex
											- rowIndex, 2), 0.5 * p);
					if (distance == 0)// 如果是重合点，不计入
					{
						valueTop = listNearPoint.get(k).Value;
						valueBottom = 1;
					} else {
						valueTop += (listNearPoint.get(k).Value / distance);
						valueBottom += 1 / distance;
					}
				}
				arrValue[rowIndex][colIndex] = valueTop / valueBottom;
			}
		}

		return arrValue;
	}
	public String getIDWValue(List<GisPoint> listGisPoint, int rowCount,
			int columnCount, int nearPointCount) {
		if (listGisPoint.size() <= 0) {
            return null;
        }

		double[] arrXY = null;
		for (GisPoint point : listGisPoint) {
			arrXY = ConvertToRowCol(point.Lon, point.Lat);
			point.ColIndex = arrXY[0];
			point.RowIndex = arrXY[1];
		}

		StringBuilder sb=new StringBuilder();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)// 纵轴
		{
			for (int colIndex = 0; colIndex < columnCount; colIndex++)// 横轴
			{
				double valueTop = 0;
				double valueBottom = 0;

				List<GisPoint> listNearPoint = GetNearPoint(listGisPoint,
						rowIndex, colIndex, nearPointCount);

				for (int k = 0; k < listNearPoint.size(); k++) {
					// 计算距离，这里取P为2，如果调整，需要开方后在幂运算
					double p = 2;
					double distance = Math.pow(
							Math.pow(listNearPoint.get(k).ColIndex - colIndex,
									2)
									+ Math.pow(listNearPoint.get(k).RowIndex
											- rowIndex, 2), 0.5 * p);
					if (distance == 0)// 如果是重合点，不计入
					{
						valueTop = listNearPoint.get(k).Value;
						valueBottom = 1;
					} else {
						valueTop += (listNearPoint.get(k).Value / distance);
						valueBottom += 1 / distance;
					}
				}
				if(sb.length()>0){
					sb.append(",");
				}
				sb.append(valueTop / valueBottom);
			}
		}

		return sb.toString();
	}

	// / <summary>
	// / 获取最近点集合
	// / </summary>
	// / <param name="listGisPoint">待插值点集合</param>
	// / <param name="currentRowIndex">当前行数索引</param>
	// / <param name="currentColIndex">当前列数索引</param>
	// / <param name="nearPointCount">做权重分析的最近点数</param>
	// / <returns></returns>
	public List<GisPoint> GetNearPoint(List<GisPoint> listGisPoint, double currentRowIndex, double currentColIndex, int nearPointCount)
        {
            for (int i = 0; i < listGisPoint.size(); i++)
            {
                //计算距离，这里取P为2，如果调整，需要开方后在幂运算
                double p = 2;
                listGisPoint.get(i).Distance = Math.pow(Math.pow(listGisPoint.get(i).ColIndex - currentColIndex, 2) + Math.pow(listGisPoint.get(i).RowIndex - currentRowIndex, 2), 0.5 * p);
            }
            //listGisPoint = listGisPoint.OrderBy(o => o.Distance).ToList();

            Collections.sort(listGisPoint, new Comparator<GisPoint>() {
            	
                public int compare(GisPoint o1, GisPoint o2) {
                
                	/*int flag=-1;
                	if(o1.Distance-o2.Distance>=0){
                		flag=1;
                	}else{
                		flag = -1;
                	}*/
                	Double d1 = o1.Distance;
                	Double d2 = o2.Distance;
                    return d1.compareTo(d2);
                }
            });
            
            List<GisPoint> listNearPoint = new ArrayList<GisPoint>();
            if (nearPointCount > listGisPoint.size())
            {
                nearPointCount = listGisPoint.size();
            }

            for (int i = 0; i < nearPointCount; i++)
            {
                listNearPoint.add(listGisPoint.get(i));
            }

            return listNearPoint;
        }

	// / <summary>
	// / 经纬度转换为行列
	// / </summary>
	// / <param name="lon">经度</param>
	// / <param name="lat">纬度</param>
	// / <returns>行、列索引</returns>
	private double[] ConvertToRowCol(double lon, double lat) {
		double rowIndex = (lon - _beginLon) / _size;
		double colIndex = (lat - _beginLat) / _size;
		double[] arrRowCol = { rowIndex, colIndex };
		return arrRowCol;
	}
}
