package com.cpstudio.zhuojiaren.util;

import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;

import com.cpstudio.zhuojiaren.model.UserVO;
import com.utils.ContactLocaleUtils;
import com.utils.ContactLocaleUtils.FullNameStyle;

public class AlphabetComparator implements Comparator<UserVO> {
	private RuleBasedCollator collator;

	public AlphabetComparator() {
		collator = (RuleBasedCollator) Collator
				.getInstance(java.util.Locale.CHINA);
	}

	@Override
	public int compare(UserVO user1, UserVO user2) {
		String py1 = user1.getPinyin();
		String py2 = user2.getPinyin();
		if (py1 == null || py1.equals("")) {
			py1 = ContactLocaleUtils.getIntance().getSortKey(user1.getUsername(),
					FullNameStyle.CHINESE);
			user1.setPinyin(py1);
		}
		if (py2 == null || py2.equals("")) {
			py2 = ContactLocaleUtils.getIntance().getSortKey(user2.getUsername(),
					FullNameStyle.CHINESE);
			user2.setPinyin(py2);
		}
		CollationKey c1 = collator.getCollationKey(py1);
		CollationKey c2 = collator.getCollationKey(py2);
		return collator.compare(((CollationKey) c1).getSourceString(),
				((CollationKey) c2).getSourceString());
	}
}