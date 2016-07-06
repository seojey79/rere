package com.seojey.domain.db.hbase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * Annotation for the hbase table row representation.
 * Only class can be annotated with HTableRow.
 * Any class annotated with HTable should have one field that annotated with {@link HRowKey}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface HTableRow {
	/***
	 * plain text name of the hbase table
	 * @return table name
	 */
	String of();
}
