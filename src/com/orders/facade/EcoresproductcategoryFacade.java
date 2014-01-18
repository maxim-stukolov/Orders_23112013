package com.orders.facade;

import org.orders.entity.Ecoresproductcategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class EcoresproductcategoryFacade extends AbstractFacade<Ecoresproductcategory>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    private static Logger log = Logger.getLogger(EcoresproductcategoryFacade.class.getName());
    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresproductcategoryFacade() {
        super(Ecoresproductcategory.class);
    }

    public List<Ecoresproductcategory> findByCategory(Long recid) {
        //log.info("Код категории:" + recid);
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresproductcategory.class);
        Root<Ecoresproductcategory> root = cq.from(Ecoresproductcategory.class);
        Predicate category = criteriaBuilder.equal(root.get("category"), recid);
        cq.select(root).where(category);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
