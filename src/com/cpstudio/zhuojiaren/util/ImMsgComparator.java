package com.cpstudio.zhuojiaren.util;

import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;

import com.cpstudio.zhuojiaren.model.ImMsgVO;

public class ImMsgComparator implements Comparator<ImMsgVO> {
	private RuleBasedCollator collator;

	public ImMsgComparator() {
		collator = (RuleBasedCollator) Collator
				.getInstance(java.util.Locale.CHINA);
	}

	@Override
	public int compare(ImMsgVO item1, ImMsgVO item2) {
		String o1 = item1.getAddtime();
		String o2 = item2.getAddtime();
		CollationKey c1 = collator.getCollationKey(o1);
		CollationKey c2 = collator.getCollationKey(o2);
		return collator.compare(((CollationKey) c2).getSourceString(),
				((CollationKey) c1).getSourceString());
	}
}