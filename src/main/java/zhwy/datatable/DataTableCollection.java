package zhwy.datatable;

import java.util.ArrayList;

/**
 * 数据表集合[数据集] <br>
 * 数据集是一个很大的概念,从结构上说,数据集包含很多数据表<code>DataTable</code>
 */
public class DataTableCollection
{
	/**
	 * 数据集
	 */
	private DataSet dataSet;
	/**
	 * 数据表集合
	 */
	private ArrayList<DataTable> dataTable;

	/**
	 * 收集 DataTable
	 * 
	 * @param ds
	 *            数据集
	 */
	public DataTableCollection(DataSet ds)
	{
		dataSet = ds;
		dataTable = new ArrayList<DataTable>();
	}
	/**
	 * 获取数据集
	 * 
	 * @return 数据集
	 */
	DataSet getDataSet()
	{
		return dataSet;
	}
	/**
	 * 获取数据集中数据表的个数
	 * 
	 * @return 数据表的个数
	 */
	public int getCount()
	{
		return dataTable.size();
	}
	/**
	 * 获取数据集中指定索引的数据表
	 * 
	 * @param index
	 *            索引
	 * @return 数据表
	 */
	public DataTable get(int index)
	{
		return (DataTable) dataTable.get(index);
	}
	/**
	 * 获取数据集中指定表名的数据表
	 * 
	 * @param tablename
	 *            表名
	 * @return 数据表
	 */
	public DataTable get(String tablename)
	{
		for (int i = 0; i < dataTable.size(); i++)
		{
			DataTable dt = (DataTable) dataTable.get(i);
			if (dt.getTableName() == tablename) return dt;
		}
		return null;
	}
	/**
	 * 新增一个数据表到数据集中
	 * 
	 * @param dt
	 *            新增的数据表
	 */
	public void Add(DataTable dt)
	{
		dataTable.add(dt);
	}
	/**
	 * 删除数据表集合中指定的数据表
	 * 
	 * @param dt
	 *            数据表
	 */
	public void Remove(DataTable dt)
	{
		dataTable.remove(dt);
	}
	/**
	 * 删除数据表集合中指定表名的数据表
	 * 
	 * @param tablename
	 *            表名
	 */
	public void Remove(String tablename)
	{
		DataTable dt = this.get(tablename);
		if (dt != null) this.Remove(dt);
	}
	/**
	 * 删除数据表集合中指定索引的数据表
	 * 
	 * @param index
	 *            索引
	 */
	public void Remove(int index)
	{
		dataTable.remove(index);
	}
	/**
	 * 判断数据表集合中是否包含指定表名的数据表
	 * 
	 * @param tablename
	 *            表名
	 * @return boolean
	 */
	public boolean Contains(String tablename)
	{
		DataTable dt = this.get(tablename);
		if (dt != null) return true;
		return false;
	}
}
