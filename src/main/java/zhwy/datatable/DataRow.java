package zhwy.datatable;

import java.util.ArrayList;

/**
 * 数据行
 */
public class DataRow
{
	//定义该行记录在table所处的行数
    private int rowIndex = -1; 

	/**
	 * 数据行
	 */
	private ArrayList<Object> alData;
	/**
	 * 数据表
	 */
	private DataTable dataTable;

	/**
	 * 构造器定义 +1 重载
	 * 
	 * @param values
	 */
	public DataRow(Object[] values)
	{
		this();
		for (int i = 0; i < values.length; i++)
		{
			alData.add(values[i]);
		}
	}
	/**
	 * 构造器定义
	 */
	public DataRow()
	{
		alData = new ArrayList<Object>();
	}
	/**
	 * 获取数据表
	 * 
	 * @return 数据表
	 */
	public DataTable getDataTable()
	{
		return dataTable;
	}
	/**
	 * 设置数据表
	 * 
	 * @param _DataTable
	 *            数据表
	 */
	public void setDataTable(DataTable _DataTable)
	{
		// 确保数据量和数据表的列匹配
		int count = _DataTable.getColumns().getCount();
		while (alData.size() < count)
		{
			alData.add(null);
		}
		if (alData.size() > count) alData.trimToSize();
		//
		dataTable = _DataTable;
	}
	/**
	 * 获取array类型的数据行
	 * 
	 * @return Object[]
	 */
	public Object[] getItemArray()
	{
		return alData.toArray();
	}
	/**
	 * 判断指定数据列是否有值
	 * 
	 * @param dc
	 *            数据列
	 * @return boolean
	 */
	public boolean IsNull(DataColumn dc)
	{
		int index = dc.getOrdinal();
		return IsNull(index);
	}
	/**
	 * 判断指定数据列是否有值
	 * 
	 * @param index
	 *            索引
	 * @return boolean
	 */
	public boolean IsNull(int index)
	{
		if (index == -1) return false;
		return alData.get(index) == null ? true : false;
	}
	/**
	 * 判断指定数据列是否有值
	 * 
	 * @param columnname
	 *            列名
	 * @return boolean
	 */
	public boolean IsNull(String columnname)
	{
		int index = dataTable.getColumns().IndexOf(columnname.toUpperCase());
		return IsNull(index);
	}
	/**
	 * 获取数据表中指定的数据值
	 * 
	 * @param index
	 *            索引
	 * @return Object 值,如果不存在则返回""
	 */
	public Object get(int index)
	{
		return alData.get(index) != null ? alData.get(index) : "";
	}
	/**
	 * 设置指定索引项的值
	 * 
	 * @param index
	 *            索引
	 * @param value
	 *            值
	 */
	public void set(int index, Object value)
	{
		if (alData.size() <= index)
		{
			while (alData.size() <= index)
				alData.add(null);
		}
		alData.set(index, value);
	}
	/**
	 * 获取数据表中指定的数据值
	 * 
	 * @param columnname
	 *            列名
	 * @return 数据值
	 */
	public Object get(String columnname)
	{
		int index = dataTable.getColumns().IndexOf(columnname.toUpperCase());
		return index == -1 ? null : get(index);
	}
	/**
	 * 设置该行的列值
	 * 
	 * @param columnname
	 *            列名
	 * @param value
	 *            数据值
	 */
	public void set(String columnname, Object value)
	{
		int index = dataTable.getColumns().IndexOf(columnname.toUpperCase());
		set(index, value);
	}
	/**
	 * 获取数据表中指定的数据值
	 * 
	 * @param dc
	 *            数据列
	 * @return 数据值
	 */
	public Object get(DataColumn dc)
	{
		int index = dataTable.getColumns().IndexOf(dc);
		return get(index);
	}
	/**
	 * 设置该行的列值
	 * 
	 * @param dc
	 *            数据列
	 * @param value
	 *            值
	 */
	public void set(DataColumn dc, Object value)
	{
		int index = dataTable.getColumns().IndexOf(dc);
		set(index, value);
	}
	/**  
       * 功能描述：  获取当前行的行索引
       * @param
       * @return: int    
       */
      public int getRowIndex() {
          return rowIndex;
      }

}
