package zhwy.entity;

import java.util.List;
import zhwy.datatable.DataTable;
import zhwy.datatable.JsonEasyTable;

public class ChongxianqiResult
    {
        public String Message ;
        public String MethodName;
        public List<Double> listShuJu ;
        public String Result ;
        public DataTable dtCanShu ;
        public DataTable dtP3BlackPoint ;
        public DataTable dtP3DottedLine ;
        public DataTable dtP3FullLine ;
        public JsonEasyTable dtP3Result ;
        public DataTable dtP3ResultColumn;
		public String getMessage() {
			return Message;
		}
		public void setMessage(String message) {
			Message = message;
		}
		public String getMethodName() {
			return MethodName;
		}
		public void setMethodName(String methodName) {
			MethodName = methodName;
		}
		public List<Double> getListShuJu() {
			return listShuJu;
		}
		public void setListShuJu(List<Double> listShuJu) {
			this.listShuJu = listShuJu;
		}
		public String getResult() {
			return Result;
		}
		public void setResult(String result) {
			Result = result;
		}
		public DataTable getDtCanShu() {
			return dtCanShu;
		}
		public void setDtCanShu(DataTable dtCanShu) {
			this.dtCanShu = dtCanShu;
		}
		public DataTable getDtP3BlackPoint() {
			return dtP3BlackPoint;
		}
		public void setDtP3BlackPoint(DataTable dtP3BlackPoint) {
			this.dtP3BlackPoint = dtP3BlackPoint;
		}
		public DataTable getDtP3DottedLine() {
			return dtP3DottedLine;
		}
		public void setDtP3DottedLine(DataTable dtP3DottedLine) {
			this.dtP3DottedLine = dtP3DottedLine;
		}
		public DataTable getDtP3FullLine() {
			return dtP3FullLine;
		}
		public void setDtP3FullLine(DataTable dtP3FullLine) {
			this.dtP3FullLine = dtP3FullLine;
		}
		public DataTable getDtP3ResultColumn() {
			return dtP3ResultColumn;
		}
		public void setDtP3ResultColumn(DataTable dtP3ResultColumn) {
			this.dtP3ResultColumn = dtP3ResultColumn;
		}
		public JsonEasyTable getDtP3Result() {
			return dtP3Result;
		}
		public void setDtP3Result(JsonEasyTable dtP3Result) {
			this.dtP3Result = dtP3Result;
		}
		
        
        

    }

