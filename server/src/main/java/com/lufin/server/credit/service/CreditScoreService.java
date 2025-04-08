package com.lufin.server.credit.service;

import com.lufin.server.credit.domain.CreditEventType;
import com.lufin.server.member.domain.Member;

public interface CreditScoreService {

	void applyScoreChange(Member member, int delta, CreditEventType eventType, int classId);
}
