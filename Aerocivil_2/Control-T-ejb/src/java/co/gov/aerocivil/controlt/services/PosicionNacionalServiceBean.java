/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Viviana
 */
@Stateless
public class PosicionNacionalServiceBean implements PosicionNacionalService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public PosicionNacional guardar(PosicionNacional posicion, Funcionario f) throws SQLIntegrityConstraintViolationException {
        posicion.setDepCategoria(em.find(DepCategoria.class, posicion.getDepCategoria().getDcId()));
        //System.out.println("posicion.getDepCategoria().getDcId(): " + posicion.getDepCategoria());
        if (posicion.getPnId()!=null){
            PosicionNacional posAux = em.find(PosicionNacional.class, posicion.getPnId());
            if(!posAux.getPnVencimiento().equals(posicion.getPnVencimiento())){
                actualizarVencimientosPosiciones(posicion);
            }
        }
        return (PosicionNacional) auditoria.auditar(posicion, f);
    }

    @Override
    public List<PosicionNacional> getLista(PosicionNacional posicion, Integer first, Integer pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(posicion, sortField, sortOrder);
        if (first != null && pageSize != null) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<PosicionNacional>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(PosicionNacional posicion, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder();
        //System.out.println("posicion.getDepCategoria().getDcId(): "+posicion.getDepCategoria().getDcId());
        if (posicion.getDepCategoria().getDcId() != null) {
            condiciones.add("f.depCategoria.dcId =:depCatId ");
            params.put("depCatId", posicion.getDepCategoria().getDcId());
        }
        if (posicion.getPnAlias() != null && !"".equals(posicion.getPnAlias())) {
            condiciones.add("upper(f.pnAlias) like :alias ");
            params.put("alias", "%" + posicion.getPnAlias().toUpperCase() + "%");
        }
        
        condiciones.add("f.pnEstado = 'Activo' ");

        if (condiciones.size() > 0) {
            strQry.append(" where ");
        }

        strQry.append(QueryUtil.joinWithAnd(condiciones));

        Query query = em.createQuery("Select count(f) from PosicionNacional f " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select f from PosicionNacional f ").
                append(strQry.toString());

            strQryFinal.append("order by f.pnAlias ASC ");
        
        //System.out.println("strQryFinal: "+strQryFinal+"::"+posicion.getDepCategoria().getDcId());
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
    }

    @Override
    public Long getCount() {
        return count;
    }

    /**
     * Si retorna true se puede inactivar
     * @param posicion
     * @return 
     */
    @Override
    public boolean validarInactivacion(PosicionNacional posicion) {
        StringBuilder strQry = new StringBuilder();
        strQry.append("Select count(*) from posicion where ")
                .append(" pos_posicion_nacional=").append(posicion.getPnId());
        Query q = em.createNativeQuery(strQry.toString());
        BigDecimal count = (BigDecimal) q.getSingleResult();
        //System.out.println(strQry.toString() + " -cnt: " + count);
        if (count != null && count.longValue() > 0) {
            //System.out.println("existe:::::::::::::");
            return false;
        }
        return true;
    }

    private void actualizarVencimientosPosiciones(PosicionNacional posicion) {
        StringBuilder strQry = new StringBuilder();
        strQry.append("update posicion set pos_tiempo_vence = ").
                append(posicion.getPnVencimiento()).
                append(" where pos_posicion_nacional = ").append(posicion.getPnId());
        //auditoria.auditar(strQry, null)
        //@TODO tal vez no hace falta auditar esto, con que se audite la posicionNacional es suficiente
        Query q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();       
    }

    @Override
    public PosicionNacional getPosNalById(Long posNacId) {
        return em.find(PosicionNacional.class,posNacId);
    }
}