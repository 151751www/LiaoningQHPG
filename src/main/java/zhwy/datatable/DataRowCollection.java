package zhwy.datatable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 数据行集合
 */
public class DataRowCollection implements Iterable<DataRow>
{
	/**
	 * 数据表
	 */
	private DataTable dataTable;
	/**
	 * 数据行集合
	 */
	protected ArrayList<DataRow> dataRow;

	/**
	 * 构造器定义
	 * 
	 * @param dt
	 */
	public DataRowCollection(DataTable dt)
	{
		dataRow = new ArrayList<DataRow>();
		dataTable = dt;
	}
	/**
	 * 获取数据行的个数
	 * 
	 * @return 行数
	 */
	public int getCount()
	{
		if (dataRow == null) return 0;
		return dataRow.size();
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
	 * 清除数据行中的数据
	 */
	public void Clear()
	{
		dataRow.clear();
	}
	/**
	 * 新增数据行
	 * 
	 * @param dr
	 *            数据行
	 */
	public void Add(DataRow dr)
	{
		dataRow.add(dr);
	}
	/**
	 * 新增数据行
	 * 
	 * @param values
	 *            值
	 */
	public void Add(Object[] values)
	{
		DataRow dr = new DataRow(values);
		dr.setDataTable(dataTable);
		dataRow.add(dr);
	}
	/**
	 * 判断数据行集合中是否包含指定的数据行
	 * 
	 * @param dr
	 *            数据行
	 * @return boolean
	 */
	public boolean Contains(DataRow dr)
	{
		return dataRow.contains(dr);
	}
	/**
	 * 新增数据行到指定的位置
	 * 
	 * @param dr
	 *            数据行
	 * @param pos
	 *            索引
	 */
	public void InsertAt(DataRow dr, int pos)
	{
		dataRow.add(pos, dr);
	}
	/**
	 * 删除指定的数据行
	 * 
	 * @param dr
	 *            数据行
	 */
	public void Remove(DataRow dr)
	{
		dataRow.remove(dr);
	}
	/**
	 * 删除指定的数据行
	 * 
	 * @param index
	 *            数据行索引
	 */
	public void RemoveAt(int index)
	{
		dataRow.remove(index);
	}
	/**
	 * 获取数据行
	 * 
	 * @param index
	 *            数据行索引
	 * @return 数据行
	 */
	public DataRow get(int index)
	{
		return (DataRow) dataRow.get(index);
	}
	public Iterator<DataRow> iterator()
	{
		return new Iterator<DataRow>()
		{
			private int index = 0;

			public boolean hasNext()
			{
				return index < getCount();
			}
			public DataRow next()
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
