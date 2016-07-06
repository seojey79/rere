package com.seojey.domain.db.hbase.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.seojey.domain.db.hbase.annotation.HColumn;
import com.seojey.domain.db.hbase.connection.BaseConnectionFactory;
import com.seojey.domain.db.hbase.scheme.HColumnDef;
import com.seojey.domain.db.hbase.scheme.QueryType;
import com.seojey.domain.db.hbase.tableinfo.HBaseTableType;
import com.seojey.domain.db.hbase.util.RecommendUtils;
import com.seojey.domain.type.KeyFormatType;
import com.seojey.domain.managing.exception.DbException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * HBase template abstraction.
 *
 * Annotation processor will generate templates corresponding each model derived from this class.
 *
 * set & del must use only integration test
 * - not checked about performance
 */
@Slf4j
public abstract class HBaseTemplateService<T, K> {
	private BaseConnectionFactory connectionFactory;
	private HBaseConverter<T, K> converter;
	private HBaseTableType table;

	public HBaseTemplateService(BaseConnectionFactory connectionFactory, HBaseConverter<T, K> converter, HBaseTableType table) {
		this.connectionFactory = connectionFactory;
		this.converter = converter;
		this.table = table;
		this.converter.keyFormatType = table.getKeyFormatType();
	}

	/***
	 * Check if the rowKey exists in the table.
	 * @param rowKey row's key
	 * @return
	 * @throws java.io.IOException
	 */
	public Boolean exist(K rowKey) throws IOException {
		Table hTable = connectionFactory.getInstance().getTable(TableName.valueOf(getTableName()));
		return hTable.exists(converter.toGet(rowKey, null, QueryType.NONE_PARAMETER));
	}

	/***
	 * Get single model Object by rowKey.
	 * Always returns model object.
	 * returned model object doesn't guarantee that the row having the same rowkey was actually in the table.
	 * Use exist method to check if it is in the table.
	 * @param rowKey RowKey for the hbase table.
	 * @return
	 * @throws java.io.IOException
	 * @throws com.seojey.domain.managing.exception.DbException
	 */
	public T get(K rowKey) throws IOException, DbException, IllegalAccessException {
		return get(rowKey, null, QueryType.NONE_PARAMETER);
	}

	/***
	 * Get single model Object by rowKey and ColumnFamily.
	 * Always returns model object.
	 * returned model object doesn't guarantee that the row having the same rowkey was actually in the table.
	 * Use exist method to check if it is in the table.
	 * @param rowKey RowKey for the hbase table.
	 * @param cf 	column family name in row
	 * @return
	 * @throws java.io.IOException
	 * @throws DbException
	 */
	public T getByColumnFamily(K rowKey, String cf) throws IOException, DbException, IllegalAccessException {
		return get(rowKey, Lists.newArrayList(new HColumnDef(cf)), QueryType.COLUMN_FAMILY);
	}
	public T getByColumnFamilyList(K rowKey, List<String> cfList)
		throws IOException, DbException, IllegalAccessException {
		return get(rowKey, HColumnDef.getHColumnDefList(cfList), QueryType.COLUMN_FAMILY);
	}

	public T getByColumn(K rowKey, String cf, String qualifier)
		throws IOException, DbException, IllegalAccessException {
		return get(rowKey, Lists.newArrayList(new HColumnDef(cf, qualifier)), QueryType.QUALIFIER);
	}

	/**
	 * Get single model Object by rowKey and ColumnFamily.
	 * Always returns model object.
	 * returned model object doesn't guarantee that the row having the same rowkey was actually in the table.
	 * Use exist method to check if it is in the table.
	 * @param rowKey RowKey for the hbase table.
	 * @param hColumnDefList column family name  and  qualifier name  list in row
	 * @param type		query type. Ref "QueryType" class
	 * @return			each Data type with Table
	 * @throws java.io.IOException
	 * @throws DbException
	 */
	public T get(K rowKey, List<HColumnDef> hColumnDefList, QueryType type)
		throws IOException, IllegalAccessException, DbException {
		if (rowKey == null) {
			throw new DbException("Row key is null");
		}

		Get get = converter.toGet(rowKey, hColumnDefList, type);

		try {
			Table hTable = connectionFactory.getTable(getTableName());
			Result result = hTable.get(get);
			if (result == null) {
				throw new DbException("get Result is null");
			}
			connectionFactory.close(hTable);

			T convertResult = converter.toRow(result, rowKey);

			return convertResult;

		} catch (IOException e) {
			throw new DbException("Exception on get method and return Default value." +  e.toString());
		}

	}


	public List<T> gets(List<K> keys) throws DbException, IllegalAccessException, IOException {
		return gets(keys, null, QueryType.NONE_PARAMETER);
	}

	public List<T> getsByColumnFamily(List<K> keys, String cf)
		throws IOException, DbException, IllegalAccessException {
		return gets(keys, Lists.newArrayList(new HColumnDef(cf)), QueryType.COLUMN_FAMILY);
	}

	public List<T> getsByColumnFamilyList(List<K> keys, List<String> cfList)
		throws IOException, DbException, IllegalAccessException {
		return gets(keys, HColumnDef.getHColumnDefList(cfList), QueryType.COLUMN_FAMILY);
	}

	public List<T> getsByColumn(List<K> keys, String cf, String qualifier)
		throws IOException, DbException, IllegalAccessException {
		return gets(keys, Lists.newArrayList(new HColumnDef(cf, qualifier)), QueryType.QUALIFIER);
	}
	/***
	 * Get multiple model objects by rowkeys, qualifier condition List, Query Type(not set by user)
	 * Both rowkeys and the returned objects have the same size.
	 *  - if you want to result by multiple qualifier, so use rowkey.
	 * @param keys
	 * @return
	 * @throws java.io.IOException
	 * @throws DbException
	 */
	@SuppressWarnings("unchecked")
	public List<T> gets(List<K> keys, List<HColumnDef> qualifierList, QueryType type)
		throws IOException, DbException, IllegalAccessException {
		if (CollectionUtils.isEmpty(keys)) {
			throw new DbException("key List is null or empty");
		}

		if (keys.size() == 1) {
			return Lists.newArrayList(get(keys.get(0), qualifierList, type));
		}

		List<Get> gets = converter.toGets(keys, qualifierList, type);

		try {
			List<T> rows = Lists.newArrayList();
			Table hTable = connectionFactory.getTable(getTableName());
			Result[] results = hTable.get(gets);
			connectionFactory.close(hTable);

			if (ArrayUtils.isEmpty(results)) {
				throw new DbException("get Result is null or empty");
			} else if (keys.size() != results.length) {
				throw new DbException("Inconsistent result. Required key size doesn't match to result length.");
			}

			for (int i = 0; i < results.length; i++) {
				T row = converter.toRow(results[i], keys.get(i));
				if (row != null) {
					rows.add(row);
				}
			}

			return rows;
		} catch (IOException e) {
			throw new DbException("Exception on get method and return Default value." + e.toString());
		}
	}

	protected String getTableName() {
		return this.table.getTableName();
	}

	public void set(Object key, HColumnDef columnDef, Object data) throws IOException {
		Put put = new Put(RecommendUtils.formattingKey(key, this.table.getKeyFormatType()));
		put.addImmutable(RecommendUtils.toBytes(columnDef.getFamily()), RecommendUtils.toBytes(columnDef.getQualifier()),
                RecommendUtils.toBytes(data));

		Table hTable = connectionFactory.getTable(getTableName());
		hTable.put(put);
		connectionFactory.close(hTable);
	}

	public void set(Object key, List<HColumnDef> columnDefList, List<Object> dataList) throws IOException, DbException {
		Put put = new Put(RecommendUtils.formattingKey(key, this.table.getKeyFormatType()));
		if (CollectionUtils.isEmpty(columnDefList) || CollectionUtils.isEmpty(dataList)) {
			throw new DbException("data or qualifier is null");
		}

		if (columnDefList.size() != dataList.size()) {
			throw new DbException("data and qualifier size is diff");
		}

		for (int i = 0; i < columnDefList.size(); i++) {
			HColumnDef columnDef = columnDefList.get(i);
			Object data = dataList.get(i);
			put.addImmutable(RecommendUtils.toBytes(columnDef.getFamily()), RecommendUtils.toBytes(columnDef.getQualifier()),
                    RecommendUtils.toBytes(data));
		}

		Table hTable = connectionFactory.getTable(getTableName());
		hTable.put(put);
	}

	public void del(Object key) throws IOException {
		Delete delete = new Delete(RecommendUtils.formattingKey(key, this.table.getKeyFormatType()));
		Table hTable = connectionFactory.getTable(getTableName());
		hTable.delete(delete);
		connectionFactory.close(hTable);
	}

	public void delList(List<Object> keys) throws IOException {
		if (CollectionUtils.isEmpty(keys)) {
			return;
		}
		List<Delete> deletes = Lists.newArrayList();
		for (Object key : keys) {
			deletes.add(new Delete(RecommendUtils.formattingKey(key, this.table.getKeyFormatType())));
		}
        Table hTable = connectionFactory.getTable(getTableName());
		hTable.delete(deletes);
		connectionFactory.close(hTable);
	}

	public Map<String, Field> getFieldMap() {
		return this.converter.fieldMap;
	}
	/***
	 * HBase operation converter.
	 * In order to convert model object to HBase operation ({@link Get}, {@link org.apache.hadoop.hbase.client.Put},
	 * {@link org.apache.hadoop.hbase.client.Delete})
	 * @param <T>
	 * @param <K>
	 */
	public abstract static class HBaseConverter<T, K> {
		private Map<String, Field> fieldMap = initFieldMap();
		private Map<String, List<Class>> typeArguments = initTypeArgumentMap();
		private KeyFormatType keyFormatType;

		private Get toGet(K key, List<HColumnDef> qualifierList, QueryType type) throws JsonProcessingException {

			Get get = new Get(RecommendUtils.formattingKey(key, keyFormatType));

			if (CollectionUtils.isNotEmpty(qualifierList)) {
				for (HColumnDef columnDef : qualifierList) {
					switch (type) {
						case COLUMN_FAMILY:
							get.addFamily(RecommendUtils.toBytes(columnDef.getFamily()));
							break;
						case QUALIFIER:
							get.addColumn(RecommendUtils.toBytes(columnDef.getFamily()), RecommendUtils.toBytes(columnDef.getQualifier()));
							break;
						default: break;
					}
				}
			}

			return get;
		}

		/***
		 * Convert to hbase gets from the rowkeys
		 * @param keys
		 * @return
		 * @throws com.fasterxml.jackson.core.JsonProcessingException
		 */
		public List<Get> toGets(List<K> keys, List<HColumnDef> qualifierList, QueryType type) throws JsonProcessingException {
			List<Get> gets = Lists.newArrayList();
			for (K key : keys) {
				Get get = toGet(key, qualifierList, type);
				if (get != null) {
					gets.add(get);
				}
			}

			return gets;
		}

		/***
		 * Convert to model object from the hbase get result.
		 * TODO 0.98 version 에서 cell, key value 이용 여부 따른 성능 테스트 필요.
		 * @param result 	조회 결과
		 * @param k			table Key
		 * @return T
		 * @throws java.io.IOException
		 */

		private T toRow(Result result, K k) throws IOException, IllegalAccessException {
			if (result == null) {
				return null;
			}
			T row = getRowInstance(k);
			applyColumns(row, result.listCells());

			return row;
		}

		protected Map<String, Field> initFieldMap() {

			List<Field> fields = getFieldList();
			Map<String, Field> fieldMap = Maps.newHashMap();

			for (Field field : fields) {
				HColumn annotation = field.getAnnotation(HColumn.class);
				if (annotation != null) {
					fieldMap.put(RecommendUtils.getHbaseFieldKey(annotation.cf(), annotation.col()), field);
				}
			}

			return fieldMap;
		}

		protected Map<String, List<Class>> initTypeArgumentMap() {
			List<Field> fields = getFieldList();
			Map<String, List<Class>> typeArgumentMap = Maps.newHashMap();
			for (Field field : fields) {
				HColumn annotation = field.getAnnotation(HColumn.class);
				if (annotation != null) {
					Class<?> cls = field.getType();

					if (cls.isAssignableFrom(List.class) || cls.isAssignableFrom(Map.class)) {
						List<Class> list = Lists.newArrayList();
						ParameterizedType integerListType = (ParameterizedType) field.getGenericType();

						for (int i = 0; i < integerListType.getActualTypeArguments().length; i++) {
							list.add((Class) integerListType.getActualTypeArguments()[i]);
						}
						typeArgumentMap.put(RecommendUtils.getHbaseFieldKey(annotation.cf(), annotation.col()), list);
					}
				}
			}
			return typeArgumentMap;
		}

		/**
		 * hbase 0.94 버젼에서 cell 미지원, perf 및 product server update 후 해당 기능 로직으로 변경 예정
		 * - keyValue 방식은 0.98에서 deprecated 됨.
		 *
		 */

		protected void applyColumns(T row, List<Cell> cellList) throws IOException, IllegalAccessException {

			if (CollectionUtils.isEmpty(cellList)) {
				return;
			}

			for (Cell cell : cellList) {
				String col = Bytes.toString(CellUtil.cloneQualifier(cell));
				String cf = Bytes.toString(CellUtil.cloneFamily(cell));

				Field field = fieldMap.get(RecommendUtils.getHbaseFieldKey(cf, col));
				if (field != null) {
					field.setAccessible(true);
					field.set(row, RecommendUtils.fromBytes(CellUtil.cloneValue(cell), field.getType(), typeArguments.get(cf + ":" + col)));
				}
			}
		}

//		protected void applyColumns(T row, List<KeyValue> cellList) throws IOException, IllegalAccessException {
//
//			if (CollectionUtils.isEmpty(cellList)) {
//				return;
//			}
//
//			for (KeyValue cell : cellList) {
//				String col = Bytes.toString(cell.getQualifier());
//				String cf = Bytes.toString(cell.getFamily());
//
//				Field field = fieldMap.get(RecommendUtils.getHbaseFieldKey(cf, col));
//
//				if (field != null) {
//					field.setAccessible(true);
//					field.set(row, ByteUtils.fromBytes(cell.getValue(), field.getType(),
//						typeArguments.get(RecommendUtils.getHbaseFieldKey(cf, col))));
//				}
//			}
//		}

		/**
		 * Abstract Method part
		 */
		protected abstract T getRowInstance(K k);
		protected abstract List<Field> getFieldList();
	}
}