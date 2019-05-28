/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.to.IncapacidadTO;
import co.gov.aerocivil.controlt.to.IncapacidadVista;
import co.gov.aerocivil.controlt.util.QueryUtil;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class IncapacidadServiceBean implements IncapacidadService{
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;   
    private Long count;
    private Long total;

    @Override
    public List<IncapacidadVista> getLista(IncapacidadTO incapacidadFiltro, Integer first, Integer pageSize, String sortField, String sortOrder, String groupBy) {
        
        
        StringBuilder sb = createQueryFilter(incapacidadFiltro, sortField, sortOrder, groupBy);


        /*if(sortField!=null && sortOrder!=null){
            strQryFinal.append(" order by p.").append(sortField).append(" ").append(sortOrder);
        }*/
        //System.out.println("strQryFinal::: "+sb);
        Query query = em.createNativeQuery(sb.toString());
        query.setFirstResult(first).setMaxResults(pageSize);
        //QueryUtil.setParameters(query, params);
        setRowCount(sb.toString());

        List<IncapacidadVista> listaIncapacidad = new ArrayList<IncapacidadVista>();
        List<Object[]> lista = query.getResultList();
        for(Object[] ob : lista){
            IncapacidadVista iv = new IncapacidadVista();
            iv.setCount(Long.valueOf(ob[0].toString()));
            iv.setNombre(ob[1].toString());
            listaIncapacidad.add(iv);
        }
        return listaIncapacidad;     
    }
    
    private void setRowCount(String sql){
        String qry="select count(*) from ("+sql+")";
        Query q = em.createNativeQuery(qry);
        this.count = QueryUtil.getLongNativeQuery(q);
    }
    
    
    private StringBuilder createQueryFilter(IncapacidadTO incapacidad, String sortField, 
            String sortOrder, String groupBy){
        List<String> condiciones = new ArrayList<String>();
        //verificar los campos de 

        StringBuilder strQryFinal = new StringBuilder();
        
        String selectStmt="";
        String groupByStmt="";
        if(groupBy!=null){
            if("regional".equals(groupBy)){
                selectStmt=" Select count(r.reg_id) as cnt, r.reg_nombre ";
                groupByStmt=" group by r.reg_id, r.reg_nombre ";
            }
            else if("aeropuerto".equals(groupBy)){
                selectStmt=" Select count(a.ae_id) as cnt, a.ae_nombre ";
                groupByStmt=" group by a.ae_id, a.ae_nombre ";
            }
            else if("dependencia".equals(groupBy)){
                selectStmt=" Select count(d.dep_id) as cnt, d.dep_nombre ";
                groupByStmt=" group by d.dep_id, d.dep_nombre ";
            }
            
        }
        strQryFinal.append(selectStmt).append(" from regional r, aeropuerto a, dependencia d, funcionario f, incapacidad i ");        
        strQryFinal.append(" where i.cod_empl=f.fun_id and f.fun_dependencia = d.dep_id and ");
        strQryFinal.append(" d.dep_aeropuerto=a.ae_id and a.ae_regional=r.reg_id and ");
        
        
        
        Map<String,Object> params = new HashMap<String, Object>();
        /*strQryFinal.append(" i.FEC_INIC between to_date('','dd/mm/yyyy')");        
        strQryFinal.append(" and to_date('','dd/mm/yyyy')");        */

        if(incapacidad.getFecDesd() !=null ){
            condiciones.add(" (i.FEC_INIC) between to_date('"+StringDateUtil.formatDate(incapacidad.getFecDesd())+"','dd/mm/yyyy') and to_date('"+StringDateUtil.formatDate(incapacidad.getFecHast())+"','dd/mm/yyyy')");
            //params.put("fechaIni", StringDateUtil.formatDate(incapacidad.getFecDesd())); 
            //params.put("fechaFin", StringDateUtil.formatDate(incapacidad.getFecHast())); 
            //params.put("fechaFin", incapacidad.getAudFechaFin()); 
        }
        if (incapacidad.getFuncionario().getDependencia() != null
                && incapacidad.getFuncionario().getDependencia().getDepcategoria() != null
                && incapacidad.getFuncionario().getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("d.dep_categoria = "+incapacidad.getFuncionario().getDependencia().getDepcategoria().getDcId());
            //params.put("depCat", incapacidad.getFuncionario().getDependencia().getDepcategoria().getDcId());
        }
        if (incapacidad.getFuncionario().getDependencia() != null && incapacidad.getFuncionario().getDependencia().getDepId() != null) {
            condiciones.add("d.dep_Id = "+incapacidad.getFuncionario().getDependencia().getDepId());
            //params.put("dep", incapacidad.getFuncionario().getDependencia().getDepId());
        } 
        
        if (incapacidad.getFuncionario().getDependencia() != null && 
                incapacidad.getFuncionario().getDependencia().getAeropuerto() != null && 
                incapacidad.getFuncionario().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("a.ae_Id = "+incapacidad.getFuncionario().getDependencia().getAeropuerto().getAeId());
            //params.put("aero", incapacidad.getFuncionario().getDependencia().getAeropuerto().getAeId());
        } 
        else if (incapacidad.getFuncionario().getDependencia() != null && 
                incapacidad.getFuncionario().getDependencia().getAeropuerto().getRegional() != null && 
                incapacidad.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("r.reg_Id = "+incapacidad.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            //params.put("reg", incapacidad.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        }
        if(condiciones.size()>0){            
            strQryFinal.append(QueryUtil.joinWithAnd(condiciones));
        }        

        strQryFinal.append(groupByStmt);

        /*Query query = em.createQuery("Select count(p) from IncapacidadTO p ".concat(strQry.toString()));
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();*/
        
        
        return strQryFinal;
        
    }

    /**
     *
     * @return
     */
    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Long getTotal(IncapacidadTO incapacidadFiltro, String groupBy) {
        StringBuilder sql = createQueryFilter(incapacidadFiltro, null, null, groupBy);
        String qry="select sum(tab.cnt) from ("+sql+") tab";        
        Query q = em.createNativeQuery(qry);
        return QueryUtil.getLongNativeQuery(q);
    }
    
    
    
}
