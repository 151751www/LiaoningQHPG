package zhwy.datatable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据表<br>
 * 数据表是一个二维表,是数据行和数据列的集合
 */
public class DataTable
{
	/**
	 * 数据集
	 */
	private DataSet dataSet;
	/**
	 * 数据表名称
	 */
	private String tableName = "Table";
	/**
	 * 数据行集合
	 */
	private DataRowCollection Rows;
	/**
	 * 数据列集合
	 */
	private DataColumnCollection Columns;

	public void setRows(DataRowCollection rows) {
		Rows = rows;
	}

	public void setColumns(DataColumnCollection columns) {
		Columns = columns;
	}

	/**
	 * 构造器定义
	 */
	public DataTable()
	{
		Rows = new DataRowCollection(this);
		Columns = new DataColumnCollection(this);
	}
	/**
	 * 构造器定义
	 * 
	 * @param _TableName
	 */
	public DataTable(String _TableName)
	{
		this();
		tableName = _TableName;
	}
	/**
	 * 获取数据集
	 * 
	 * @return 数据集
	 */
	public DataSet getDataSet()
	{
		return dataSet;
	}
	/**
	 * 设置数据集
	 * 
	 * @param _DataSet
	 *            数据集
	 */
	public void setDataSet(DataSet _DataSet)
	{
		dataSet = _DataSet;
	}
	/**
	 * 获取数据表名称
	 * 
	 * @return 表名
	 */
	public String getTableName()
	{
		return tableName;
	}
	/**
	 * 设置数据表名称
	 * 
	 * @param _TableName
	 *            表名
	 */
	public void setTableName(String _TableName)
	{
		tableName = _TableName;
	}
	/**
	 * 获取数据表中的数据行集合
	 * 
	 * @return 数据行集合
	 */
	public DataRowCollection getRows()
	{
		return Rows;
	}
	/**
	 * 获取数据表中的数据列集合
	 * 
	 * @return 数据列集合
	 */
	public DataColumnCollection getColumns()
	{
		return Columns;
	}
	/**
	 * 清除数据行中的数据
	 */
	public void Clear()
	{
		Rows.Clear();
	}
	/**
	 * 创建新的数据行
	 * 
	 * @return 新的数据行
	 */
	public DataRow NewRow()
	{
		DataRow dr = new DataRow();
		dr.setDataTable(this);
		return dr;
	}
	/**
	 * 根据指定的字段对datatable进行排序
	 * 
	 * @param key
	 *            排序字段
	 * @param Asc
	 *            是否为升序
	 */
	public void sort(String key, boolean Asc)
	{
		if (key == null || "".equals(key)) { return; }
		if (Columns.get(key.toUpperCase()) == null) { return; }
		boolean flag = false;
		DataRow temp = null;
		int count = Rows.getCount();
		for (int i = 0; i < count - 1; i++)
		{
			flag = false;
			for (int j = 0; j < count - i - 1; j++)
			{
				String num1=Rows.get(j).get(key).toString();//当前值
				String num2=Rows.get(j+1).get(key).toString();//下一个值
				//如果两个值都为数值时,结果返回都是1时。百分号的特别处理
				if(num1.length()!=0 && num2.length()!=0 && type(num1.replaceAll("%", ""))==1 && type(num2.replaceAll("%", ""))==1)
				{
					if (Asc ? (Double.parseDouble(num1.replaceAll("%", ""))-Double.parseDouble(num2.replaceAll("%", ""))) >= 0 : (Double.parseDouble(num1.replaceAll("%", ""))-(Double.parseDouble(num2.replaceAll("%", "")))) < 0)
					{
						temp = Rows.get(j);
						Rows.dataRow.set(j, Rows.get(j + 1));
						Rows.dataRow.set(j + 1, temp);
						flag = true;
					}
				}
				else//都是字符或者一个是字符一个是数字
				{
					if(Asc?(num1.compareTo(num2)>=0):(num1.compareTo(num2)<0))
					{
						temp = Rows.get(j);
						Rows.dataRow.set(j, Rows.get(j + 1));
						Rows.dataRow.set(j + 1, temp);
						flag = true;
					}
				}
			}
			if (!flag) break;
		}
	}
	
	/**
	 * 根据指定的字段对datatable进行排序,datatable中String转换为数值
	 * 
	 * @param key
	 *            排序字段
	 * @param Asc
	 *            是否为升序
	 */
	public void sortNumber(String key, boolean Asc)
	{
		if (key == null || "".equals(key)) { return; }
		if (Columns.get(key.toUpperCase()) == null) { return; }
		boolean flag = false;
		DataRow temp = null;
		int count = Rows.getCount();
		for (int i = 0; i < count - 1; i++)
		{
			flag = false;
			for (int j = 0; j < count - i - 1; j++)
			{
				Double nowKey = Double.parseDouble(Rows.get(j).get(key).toString());
				Double nextKey = Double.parseDouble(Rows.get(j + 1).get(key).toString());
				
				if (Asc ? nowKey > nextKey : nowKey < nextKey)
				{
					temp = Rows.get(j);
					Rows.dataRow.set(j, Rows.get(j + 1));
					Rows.dataRow.set(j + 1, temp);
					flag = true;
				}
			}
			if (!flag) break;
		}
	}
	
	/**
	 * 判断一个字符串是否全部是 
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str){Pattern pattern = Pattern.compile("[0-9]*");Matcher isNum = pattern.matcher(str);if( !isNum.matches() ){return false;}return true;}
	/**
	 * 判断是否是数值
	 * @param source
	 * @return 返回0非数值，返回1是int类型，返回2是double类型  更改后，返回0，非数值，返回1是数值
	 */
	 public static int type(String source){
         int result = 0;
         String regex1 = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
         boolean flag1 = source.matches(regex1);
         if(flag1){
//            String regex2 = "^[-+]?([0-9]+)$";
//             boolean flag2 = source.matches(regex2);
//             if(flag2){
//                 result = 1;
//             }
//             else{
//                 result = 2;
//             }
        	 result=1;
         }
         return result;         
     }

	/**
	 * 根据指定的字段对datatable进行升序排序 <br>
	 * 相当执行 order by <code>key</code> asc
	 * 
	 * @param key
	 *            排序字段
	 */
	public void sort(String key)
	{
		sort(key, true);
	}
}
