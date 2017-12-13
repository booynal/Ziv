/**
 * MyAvg.java
 */
package com.ziv.hive.udaf;

import java.util.List;

import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFParameterInfo;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFResolver2;
import org.apache.hadoop.hive.ql.util.JavaDataModel;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.DoubleWritable;

/**
 * TODO 未通过验证
 * 
 * @author ziv
 * @date 2017年12月11日 上午12:57:52
 */
public class MyAvg2 implements GenericUDAFResolver2 {

	@Override
	public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException {
		if (parameters.length != 1) {
			throw new UDFArgumentTypeException(parameters.length - 1, "Exactly one argument is expected.");
		}

		if (parameters[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
			throw new UDFArgumentTypeException(0, "Only primitive type arguments are accepted but " + parameters[0].getTypeName() + " is passed.");
		}

		switch (((PrimitiveTypeInfo) parameters[0]).getPrimitiveCategory()) {
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
			return new MyAvgIntUdafEvaluator();
		case TIMESTAMP:
		case FLOAT:
		case DOUBLE:
		case STRING:
		case VARCHAR:
		case CHAR:
		case DECIMAL:
		case BOOLEAN:
		case DATE:
		default:
			throw new UDFArgumentTypeException(0, "Only numeric or string type arguments are accepted but " + parameters[0].getTypeName() + " is passed.");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo info) throws SemanticException {
		if (null == info) {
			System.err.println("getEvaluator(GenericUDAFParameterInfo) - 参数为null");
			return null;
		} else {
			return getEvaluator(info.getParameters());
		}
	}

	public static class MyAvgIntUdafEvaluator extends GenericUDAFEvaluator {

		private DoubleWritable result;

		static class MyAvgBuffer extends AbstractAggregationBuffer {

			long count;
			double sum;

			@Override
			public int estimate() {
				return JavaDataModel.PRIMITIVES2 * 2;
			}
		}

		static class MyAvgObjectInspector extends StructObjectInspector {

			@Override
			public Category getCategory() {
				return Category.STRUCT;
			}

			@Override
			public String getTypeName() {
				return MyAvgBuffer.class.getName();
			}

			@Override
			public List<? extends StructField> getAllStructFieldRefs() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getStructFieldData(Object arg0, StructField arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public StructField getStructFieldRef(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Object> getStructFieldsDataAsList(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

		}

		@Override
		public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
			super.init(m, parameters);
			result = new DoubleWritable();
			if (Mode.PARTIAL1 == m || Mode.PARTIAL2 == m) {
				return new MyAvgObjectInspector();
			} else {
				return PrimitiveObjectInspectorFactory.writableDoubleObjectInspector;
			}
		}

		@Override
		public AggregationBuffer getNewAggregationBuffer() throws HiveException {
			return new MyAvgBuffer();
		}

		@Override
		public void reset(AggregationBuffer agg) throws HiveException {
			if (null != agg) {
				((MyAvgBuffer) agg).count = 0;
				((MyAvgBuffer) agg).sum = 0;
			}
		}

		@Override
		public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
			if (null != agg && null != parameters) {
				((MyAvgBuffer) agg).count++;
				if (parameters.length >= 1) {
					Object object = parameters[0];
					if (object instanceof Number) {
						((MyAvgBuffer) agg).sum += ((Number) object).doubleValue();
					} else if (null != object) {
						try {
							((MyAvgBuffer) agg).sum += Double.parseDouble(object.toString());
						} catch (NumberFormatException e) {
							throw new HiveException(String.format("平均数迭代器-只接受数字类型的参数，收到的参数: '(%s)%s'", null == object ? null : object.getClass(), object), e);
						}
					}
				}
			}
		}

		@Override
		public Object terminatePartial(AggregationBuffer agg) throws HiveException {
			return agg;
		}

		@Override
		public void merge(AggregationBuffer agg, Object partial) throws HiveException {
			if (null != agg && null != partial) {
				((MyAvgBuffer) agg).count += ((MyAvgBuffer) partial).count;
				((MyAvgBuffer) agg).sum += ((MyAvgBuffer) partial).sum;
			}
		}

		@Override
		public Object terminate(AggregationBuffer agg) throws HiveException {
			result.set(((MyAvgBuffer) agg).sum / ((MyAvgBuffer) agg).count);
			return result;
		}
	}

}
