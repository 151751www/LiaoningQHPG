package zhwy.datatable;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * 数据列集合
 */
public class DataColumnCollection implements Iterable<DataColumn>
{
	/**
	 * 数据表
	 */
	private DataTable dataTable;
	/**
	 * 数据列集合
	 */
	private ArrayList<DataColumn> dataColumn;

	public DataColumnCollection(DataTable dt)
	{
		dataColumn = new ArrayList<DataColumn>();
		dataTable = dt;
	}
	/**
	 * 获取数据列的个数
	 * 
	 * @return 数据列的个数
	 */
	public int getCount()
	{
		if (dataColumn == null) return 0;
		return dataColumn.size();
	}
	/**
	 * 获取数据表
	 * 
	 * @return 数据表
	 */
	DataTable getDataTable()
	{
		return dataTable;
	}
	/**
	 * 新增列
	 * 
	 * @param dc
	 *            数据列
	 */
	public void Add(DataColumn dc)
	{
		dc.setTable(this.dataTable);
		dataColumn.add(dc);
	}
	/**
	 * 新增列
	 * 
	 * @param _ColumnName
	 *            列名
	 */
	public void Add(String _ColumnName)
	{
		DataColumn dc = new DataColumn(_ColumnName);
		dataColumn.add(dc);
	}
	/**
	 * 新增列
	 * 
	 * @param _ColumnName
	 *            列名
	 */
	public void Add(String _ColumnName,String _Caption)
	{
		DataColumn dc = new DataColumn(_ColumnName,_Caption);
		dataColumn.add(dc);
	}
	/**
	 * 新增列
	 * 
	 * @param _ColumnName
	 *            列名
	 * @param _DataType
	 *            数据类型
	 */
	public void Add(String _ColumnName, Class<?> _DataType)
	{
		DataColumn dc = new DataColumn(_ColumnName, _DataType);
		dataColumn.add(dc);
	}
	/**
	 * 数据列集合中是否包含指定列
	 * 
	 * @param dc
	 *            数据列
	 * @return true该列在表内，false该列不在表内
	 */
	public boolean Contains(DataColumn dc)
	{
		return dataColumn.contains(dc);
	}
	/**
	 * 数据列集合中是否包含指定列
	 * 
	 * @param columnname
	 *            列名
	 * @return true该列在表内，false该列不在表内
	 */
	public boolean Contains(String columnname)
	{
		int index = this.IndexOf(columnname);
		if (index > -1)
			return true;
		else
			return false;
	}
	/**
	 * 获取指定列在数据列集合中的索引
	 * 
	 * @param dc
	 * @return 列索引
	 */
	public int IndexOf(DataColumn dc)
	{
		return dataColumn.indexOf(dc);
	}
	/**
	 * 获取指定列在数据列集合中的索引
	 * 
	 * @param columnname
	 *            列名
	 * @return 列索引
	 */
	public int IndexOf(String columnname)
	{
		for (int i = 0; i < dataColumn.size(); i++)
		{
			DataColumn dc = (DataColumn) dataColumn.get(i);
			if (dc.getColumnName().equals(columnname)) return i;
		}
		return -1;
	}
	/**
	 * 从数据列集合中移除指定列
	 * 
	 * @param dc
	 *            数据列
	 */
	public void Remove(DataColumn dc)
	{
		dataColumn.remove(dc);
	}
	/**
	 * 从数据列集合中移除指定列
	 * 
	 * @param columnname
	 *            列名
	 */
	public void Remove(String columnname)
	{
		int index = this.IndexOf(columnname);
		if (index > -1) this.RemoveAt(index);
	}
	/**
	 * 从数据列集合中移除指定列
	 * 
	 * @param index
	 *            列索引
	 */
	public void RemoveAt(int index)
	{
		dataColumn.remove(index);
	}
	/**
	 * 获取指定数据列
	 * 
	 * @param index
	 *            列索引
	 * @return 数据列
	 */
	public DataColumn get(int index)
	{
		return (DataColumn) dataColumn.get(index);
	}
	/**
	 * 获取指定数据列
	 * 
	 * @param columnname
	 *            列名
	 * @return 数据列
	 */
	public DataColumn get(String columnname)
	{
		int index = this.IndexOf(columnname);
		if (index > -1) return (DataColumn) dataColumn.get(index);
		return null;
	}
	public Iterator<DataColumn> iterator()
	{
		return new Iterator<DataColumn>()
		{
			private int index = 0;

			public boolean hasNext()
			{
				return index < getCount();
			}
			public DataColumn next()
			{
				return get(index++);
			}
			public void remove()
			{
				RemoveAt(index--);
			}
		};
	}
}
