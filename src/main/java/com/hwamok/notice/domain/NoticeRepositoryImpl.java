package com.hwamok.notice.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.stream.Collectors;

public class NoticeRepositoryImpl implements NoticeRepositoryCustom{
    private JPAQueryFactory jpaQueryFactory;
    private static final QNotice notice = QNotice.notice;
    public NoticeRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Notice> getNotices(String keyword, String filter, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(eqKeywordAndFilter(keyword, filter));

        return new PageImpl<>(
                jpaQueryFactory
                        .selectFrom(notice)
                        .where(booleanBuilder)
                        .orderBy(notice.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .stream().collect(Collectors.toList()),
                pageable,
                jpaQueryFactory
                        .select(notice.id)
                        .from(notice)
                        .where(booleanBuilder)
                        .stream().count()
        );
    }

    private BooleanExpression eqKeywordAndFilter(String keyword, String filter){
        if(Strings.isBlank(keyword)){
            return null;
        } else {
            if (filter.equals("제목")) {
                return notice.title.contains(keyword);
                
            } else if (filter.equals("내용")) {
                return notice.content.contains(keyword);
            
            // filter.equals("전체")
            } else {
                return notice.title.contains(keyword).or(notice.content.contains(keyword));
            }
        }
    }
}
