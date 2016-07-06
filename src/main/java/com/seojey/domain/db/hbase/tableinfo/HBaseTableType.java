package com.seojey.domain.db.hbase.tableinfo;

import com.seojey.domain.type.KeyFormatType;
import lombok.Getter;

@Getter
public enum HBaseTableType {
	PROFILE_USER(HBaseTable.PROFILE_USER, KeyFormatType.REVERSE),
	PROFILE_DEAL(HBaseTable.PROFILE_DEAL, KeyFormatType.REVERSE),
	PROFILE_PRODUCT(HBaseTable.PROFILE_PRODUCT, KeyFormatType.MD5),
	SEGMENT(HBaseTable.SEGMENT, KeyFormatType.MD5);

	private String tableName;
	private KeyFormatType keyFormatType;

	private HBaseTableType(String tableName, KeyFormatType type) {
		this.tableName = tableName;
		this.keyFormatType = type;
	}
}
