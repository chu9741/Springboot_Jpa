package com.example.jpashop.Repository;

import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.domain.QMember;
import com.example.jpashop.domain.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.internal.predicate.BooleanExpressionPredicate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.example.jpashop.domain.QMember.member;
import static com.example.jpashop.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @PersistenceContext
    private final EntityManager em;
    JPAQueryFactory query = new JPAQueryFactory(em);
    public List<Order> findAll(OrderSearch orderSearch){
        QMember member = QMember.member;
        QOrder order = QOrder.order;


        return query.select(order).from(order)
                .join(order.member,member)
                .where(statusEq(orderSearch.getOrderStatus())
                        ,nameLike(orderSearch.getMemberName()))
                .limit(1000).fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond){
        if(statusCond == null){
            return null;
        }
        return order.status.eq(statusCond);
    }
    private BooleanExpression nameLike(String nameCond){
        if (!StringUtils.hasText(nameCond)){
            return null;
        }
        return member.name.like(nameCond);
    }
    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }
}
