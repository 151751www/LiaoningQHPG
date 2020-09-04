package zhwy.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import zhwy.gis.GisPoint;
import zhwy.gis.InterpolationManager;
import zhwy.util.MakeGradsFile;

@Service
@PropertySource("classpath:address.properties")
public class HuiTuService {
	private static Logger logger = LoggerFactory.getLogger(HuiTuService.class);

	@Autowired
	MakeGradsFile mgf;

	/**
	 * 绘图
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String makeYuanDianTu(JSONArray jsonData,String area,String yaosu,String peise) throws Exception{

		/*String city=SwithChCityToEnCity(area);
		String latLonStr = getLatLonJSON().getString(city);//陕西省：106.1|111.1|31.9|39.1     汾渭平原：106.5|113.6|33.7|38.5*/
		//经纬度

		//获取系统时间作为文件名
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssddd");
		String time = sdf.format(curDate);

		//站点信息txt	(站名 经度 纬度 值)
		boolean isSuccess= mgf.getTxt(jsonData,time,"lon","lat","value");
		String picPath="";
		if(isSuccess){
			//绘图
			int count=jsonData.size()+1;
			picPath = mgf.drawYuanDianPic(area,time,count);
		}
		return picPath;
	}
	/**
	 * 绘图
	 * @param latLonJSON
	 * @param colorStr
	 * @return
	 * @throws Exception
	 */
	public String makePic(JSONArray latLonJSON,String colorStr,String area) throws Exception{
		//站点数据
		GisPoint gs = null;
		String picPath="";
		try {
			if(latLonJSON.toString().contains("win_d")){
				List<GisPoint> listGisPoint_Fs = new ArrayList<GisPoint>();
				List<GisPoint> listGisPoint_Fx = new ArrayList<GisPoint>();
				for (int i = 0; i < latLonJSON.size(); i++) {
					//String stationName = latLonJSON.getJSONObject(i).getString("stationName");
					Double lon = Double.parseDouble(latLonJSON.getJSONObject(i).getString("lon"));
					Double lat = Double.parseDouble(latLonJSON.getJSONObject(i).getString("lat"));
//				Double value = Double.parseDouble(latLonJSON.getJSONObject(i).getString("风速"));
					String strfx=latLonJSON.getJSONObject(i).getString("win_d");
					String strfs=latLonJSON.getJSONObject(i).getString("win_s");
					Double fx=null,fs=null;
					if(strfx.length()>0 && strfs.length()>0){
						//风向
						fx=Double.parseDouble(latLonJSON.getJSONObject(i).getString("win_d"));
						gs = new GisPoint("", lon, lat, fx);
						listGisPoint_Fx.add(gs);
						//风速
						fs=Double.parseDouble(latLonJSON.getJSONObject(i).getString("win_s"));
						gs = new GisPoint("", lon, lat, fs);
						listGisPoint_Fs.add(gs);
					}
				}
				BigDecimal minLon =new BigDecimal(area.split("\\|")[0]);
				BigDecimal maxLon =new BigDecimal(area.split("\\|")[1]);
				BigDecimal minLat =new BigDecimal(area.split("\\|")[2]);
				BigDecimal maxLat =new BigDecimal(area.split("\\|")[3]);
				BigDecimal step=new BigDecimal("0.05");

				int lonNum = maxLon.subtract(minLon).divide(step).intValue() + 1;  //134    135
				int latNum = maxLat.subtract(minLat).divide(step).intValue() + 1;  //145    147

				//插值
				InterpolationManager im = new InterpolationManager(minLon.doubleValue(),minLat.doubleValue(), 0.05);

				double[][] arrValue_Fs = im.IDW(listGisPoint_Fs,latNum,lonNum, 5);
				double[][] arrValue_Fx = im.IDW(listGisPoint_Fx,latNum,lonNum, 5);


				//绘图
				picPath = mgf.drawWindPic(latLonJSON.toString(), arrValue_Fs,arrValue_Fx, area,colorStr,step+"",latLonJSON);
			}
			else{
				List<GisPoint> listGisPoint = new ArrayList<GisPoint>();
				for (int i = 0; i < latLonJSON.size(); i++) {
					//String stationName = latLonJSON.getJSONObject(i).getString("stationName");
					Double lon = Double.parseDouble(latLonJSON.getJSONObject(i).getString("lon"));
					Double lat = Double.parseDouble(latLonJSON.getJSONObject(i).getString("lat"));
//				Double value = Double.parseDouble(latLonJSON.getJSONObject(i).getString("value"));
					Double value = null;
					String val = latLonJSON.getJSONObject(i).getString("value");
					if(val==null||val.equals("")){
						continue;
					}else{
						value=Double.parseDouble(latLonJSON.getJSONObject(i).getString("value"));
					}
					gs = new GisPoint("", lon, lat, value);
					listGisPoint.add(gs);
				}
				/*String city=SwithChCityToEnCity(area);
				String str = getLatLonJSON().getString(city);//陕西省：106.1|111.1|31.9|39.1     汾渭平原：106.5|113.6|33.7|38.5*/
				BigDecimal minLon =new BigDecimal(area.split("\\|")[0]);
				BigDecimal maxLon =new BigDecimal(area.split("\\|")[1]);
				BigDecimal minLat =new BigDecimal(area.split("\\|")[2]);
				BigDecimal maxLat =new BigDecimal(area.split("\\|")[3]);
				BigDecimal step=new BigDecimal("0.05");

				int lonNum = maxLon.subtract(minLon).divide(step).intValue() + 2;  //134    135
				int latNum = maxLat.subtract(minLat).divide(step).intValue() + 2;  //145    147

				//插值
				InterpolationManager im = new InterpolationManager(minLon.doubleValue(),minLat.doubleValue(), 0.05);

				double[][] arrValue = im.IDW(listGisPoint,latNum,lonNum, 5);

				picPath = mgf.drawGeDianPic(arrValue,area,colorStr,"0.05",latLonJSON);
			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return picPath;
	}
}
