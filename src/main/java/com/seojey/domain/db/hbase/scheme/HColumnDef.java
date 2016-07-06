package com.seojey.domain.db.hbase.scheme;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by coupang on 2015. 8. 27..
 */
@Getter
public class HColumnDef {
	private String family;
	private String qualifier;
	private HColumnDef() {

	}

	public HColumnDef(String family, String qualifier) {
		this.family = family;
		this.qualifier = qualifier;
	}

	public HColumnDef(String family) {
		this.family = family;
	}

	public static List<HColumnDef> getHColumnDefList(List<String> cfList) {
		List<HColumnDef> result = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(cfList)) {
			for (String cf : cfList) {
				result.add(new HColumnDef(cf));
			}

		}
		return result;
	}
}