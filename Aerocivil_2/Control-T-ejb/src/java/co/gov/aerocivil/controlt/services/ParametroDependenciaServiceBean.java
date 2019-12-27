/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Menu;
import co.gov.aerocivil.controlt.entities.ParametrosDependencia;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
 * @author juancamilo
 */
@Stateless
public class ParametroDependenciaServiceBean implements ParametroDependenciaService {

    private Long count;
    @EJB
    private AuditoriaService auditoria;
    
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @Override
    public List<ParametrosDependencia> listarTiposParametros() {
        Query query = em.createNamedQuery("ParametrosDependencia.findTypes");
        return query.getResultList();
    }

    @Override
    public List<ParametrosDependencia> listarTiposParametrosDependencia(DepCategoria categoria, String menuId) {
        Query query = em.createNamedQuery("ParametrosDependencia.findByCategoryModule");
        query.setParameter("catId", categoria.getDcId());
        query.setParameter("menId", menuId);
        return query.getResultList();
    }

    @Override
    public List<ParametrosDependencia> listarParametrosPorTipoDependencia(ParametrosDependencia padre) {
        Query query = em.createNamedQuery("ParametrosDependencia.findTypesByDep");
        query.setParameter("idCategoria", padre.getCategoria().getDcId());
        query.setParameter("padre", padre.getPdId());
        return query.getResultList();
    }

    @Override
    public ParametrosDependencia guardaParametroDependencia(ParametrosDependencia parametrosDependencia, Funcionario funcionario) {
        return (ParametrosDependencia) auditoria.auditar(parametrosDependencia, funcionario);
    }

    @Override
    public boolean borrarParametroDependencia(ParametrosDependencia parametrosDependencia) {
        try {
            em.remove(parametrosDependencia);
            return true;
        } catch (Exception e) {
            return false;
        }
            
    }
    
    @Override
    public List<ParametrosDependencia> getLista(ParametrosDependencia dependencia, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(dependencia, sortField, sortOrder);
        query.setFirstResult(first).setMaxResults(pageSize);
        try{
            return (List<ParametrosDependencia>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    private Query createQueryFilter(ParametrosDependencia parametroDependencia, String sortField, String sortOrder){
        
        Map<String,Object> params = new HashMap<String, Object>();
        
            List<String> condiciones = new ArrayList<String>();
            

            if (parametroDependencia.getNombre() != null) {
                condiciones.add("upper(d.nombre) like :nombre ");
                params.put("nombre", "%" + parametroDependencia.getNombre().toUpperCase() + "%");
            }
            if (parametroDependencia.getModulo() != null) {
                condiciones.add("upper(d.modulo) like :modulo ");
                params.put("modulo", "%" + parametroDependencia.getModulo().toUpperCase() + "%");
            }
            if (parametroDependencia.getCategoria().getDcId() != null) {
                condiciones.add("d.categoria.dcId = :categoria ");
                params.put("categoria", parametroDependencia.getCategoria().getDcId() );
            }
            
            StringBuilder strQry = new StringBuilder();
            if(condiciones.size()>0){
                strQry.append("where ");
            }
            for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
                strQry.append(it.next());
                if(it.hasNext()){
                    strQry.append(" and ");
                }
            }

        Query query = em.createQuery("Select count(d) from ParametrosDependencia d "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select d from ParametrosDependencia d ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by d.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
        
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public List<ParametrosDependencia> getLista(Funcionario funcionario) {
        DepCategoria categoria = funcionario.getDependencia().getDepcategoria();
        Query query = em.createNamedQuery("ParametrosDependencia.findByCategory");
        query.setParameter("catId", categoria.getDcId());
        return query.getResultList();
    }

    @Override
    public List<Menu> listarModulos() {
        String sql = "select m1.MEN_ID,m2.MEN_LABEL||' - '||m1.men_label modulo  from menu m1 "+
                    "join (select * from menu where MEN_METODO is null) m2 on SUBSTR(m1.MEN_POSICION,1,instr(m1.MEN_POSICION,',')-1) = m2.MEN_POSICION "
                + "where m1.men_label in ('Evaluación Competencias','Curso Recurrente')";
        List<Menu> modulos = new ArrayList<Menu>();
        Query q = em.createNativeQuery(sql);
        List<Object[]> list = q.getResultList();
        for(Object[] objects : list){
            Menu modulo = new Menu();
            modulo.setMenId(((BigDecimal)objects[0]).longValue());
            modulo.setMenLabel((String)objects[1]);
            modulos.add(modulo);
        }
        return modulos;
    }
    
    
}
