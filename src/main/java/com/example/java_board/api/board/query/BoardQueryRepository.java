package com.example.java_board.api.board.query;
import com.example.java_board.api.board.dto.request.SearchCondition;
import com.example.java_board.api.board.dto.request.SortCondition;
import com.example.java_board.api.board.dto.request.SortType;
import com.example.java_board.api.board.dto.response.QSelectBoard;
import com.example.java_board.api.board.dto.response.SelectBoard;
import com.example.java_board.api.board.exception.NotFoundBoardException;
import com.example.java_board.api.user.dto.response.QSelectUser;
import com.example.java_board.repository.board.QBoard;
import com.example.java_board.repository.board.QBoardLike;
import com.example.java_board.repository.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.java_board.repository.board.QBoardTag.boardTag;
import static com.example.java_board.repository.tag.QTag.tag;

@RequiredArgsConstructor
public class BoardQueryRepository {
    // em 파라미터 없이 생성 - 현재 쓰레드에 바인딩된 EntityManager 내부적으로 사용
    private final JPAQueryFactory queryFactory;

    // Board 전체 조회
    QBoard board = new QBoard("board"); // alias: board
    QBoardLike boardLike = new QBoardLike("boardLike");
    QUser user = new QUser("user");

    private JPAQuery<?> getBaseQuery(){
        return queryFactory
                .from(board)
                .leftJoin(board.likes, boardLike)
                .leftJoin(board.tags, boardTag)
                .leftJoin(boardTag.id.tag, tag);
    }

    // 게시글 전체 조회
    List<SelectBoard> findBoards(BooleanBuilder builder, List<OrderSpecifier> orderSpecifiers, NumberPath<Integer> numberPath, Pageable pageable){
        return this.getBaseQuery()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]:: new))
                .transform(GroupBy.groupBy(board.id).list(
                    new QSelectBoard(
                            board,
                            // board(N): user(1)이고, board는 user.id를 FK로 들고 있다. -> 따라서, 이를 통해 User를 참조하므로, leftJoin(user)는 필요 없음.
                            new QSelectUser(
                                    user.id,
                                    user.email
                            ),
                            numberPath != null ? board.likes.size().as(numberPath) : board.likes.size(),
                            GroupBy.list(tag.name)
                    )
                ));
    }

    // 게시글 단일 조회 by id
    SelectBoard getBoardById(Long boardId) {
        Map<Long, SelectBoard> selectBoard =
                this.getBaseQuery()
                .where(board.id.eq(boardId))
                .transform(GroupBy.groupBy(board.id).as(
                        new QSelectBoard(
                                board,
                                new QSelectUser(
                                        user.id,
                                        user.email
                                ),
                                board.likes.size(),
                                GroupBy.list(tag.name)
                        )
                ));

        if (selectBoard.isEmpty()){
            throw new NotFoundBoardException();
        }
        return selectBoard.values().iterator().next();
    }


    // 게시글 다중 조회시 페이징
    public Page<SelectBoard> findPage(SearchCondition searchCondition, SortCondition sortCondition, Pageable pageable){
        // 검색 조건 (없을 경우 null 반환)
        BooleanBuilder builder = getCondition(searchCondition);

        // 정렬 조건 (없을 경우 null 반환)
        OrderSpecifierResult orderSpecifierResult = new OrderSpecifierResult(sortCondition);
        List<OrderSpecifier> orderSpecifiers = orderSpecifierResult.getOrderSpecifiers();
        NumberPath<Integer> numberPath = orderSpecifierResult.getNumberPath();

        List<SelectBoard> content = findBoards(builder, orderSpecifiers, numberPath, pageable);
        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    private BooleanBuilder getCondition(SearchCondition searchCondition){
        if (searchCondition == null)  // 검색 조건 없음(전체 조회)
        {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();
        switch (searchCondition.type()){
            case TITLE   -> builder = builder.and(board.title.contains(searchCondition.keyword()));
            case CONTENT -> builder = builder.and(board.content.contains(searchCondition.keyword()));
            case TAG     -> builder = builder.and(tag.name.contains(searchCondition.keyword()));
            case USER    -> builder = builder.and(user.email.contains(searchCondition.keyword()));
        }
        return builder;
    }

    private class OrderSpecifierResult{
        private final List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        private NumberPath<Integer> numberPath = null;

        public OrderSpecifierResult(SortCondition sortCondition){
            if (sortCondition == null){
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, board.id));
            }
            else {
                for (SortType type: sortCondition.sortTypes()){
                    switch (type){
                        case NEW        -> orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, board.id));
                        case OLD        -> orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, board.id));
                        case MOST_LIKE  -> {
                            numberPath = Expressions.numberPath(Integer.class, "likesCount");
                            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, numberPath));
                        }
                    }
                }
            }
        }

        public List<OrderSpecifier> getOrderSpecifiers(){
            return orderSpecifiers;
        }

        public NumberPath<Integer> getNumberPath(){
            return numberPath;
        }

    }

}
