/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PosicionHabilitadaServiceBean implements PosicionHabilitadaService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    private StringBuilder strQry;
    private Query query;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public PosicionHabilitada guardar(PosicionHabilitada posicionHabilitada, Funcionario f) throws SQLIntegrityConstraintViolationException {
        Posicion p = em.find(Posicion.class, posicionHabilitada.getPosicion().getPosId());
        int dias = Integer.parseInt(p.getPosTiempoVence().toString());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, dias);
        posicionHabilitada.setPhFvencimiento(c.getTime());

        return (PosicionHabilitada) auditoria.auditar(posicionHabilitada, f);
    }

    @Override
    public void eliminar(Long[] listPosiciones, Funcionario funcionario, Funcionario funcSesion) {
        
        /*//System.out.println("El arreglo tiene: "+listPosiciones.length);
        if(listPosiciones.length > 0)
        {
            strQry = new StringBuilder();
            strQry.append("delete from PosicionHabilitada d where d.funcionario.funId = '").
                    append(funcionario.getFunId()).append("' and d.posicion.posId not in:posiciones ");
            //System.out.println("DelQry: " + strQry.toString());
            query = em.createQuery(strQry.toString());
            query.setParameter("posiciones", Arrays.asList(listPosiciones));
            query.executeUpdate();
        }
        else
        {
            strQry = new StringBuilder();
            strQry.append("delete from PosicionHabilitada d where d.funcionario.funId = '").
                    append(funcionario.getFunId()).append("'");           
            query = em.createQuery(strQry.toString());         
            query.executeUpdate();
        }*/
           
        strQry = new StringBuilder();
        strQry.append("delete from PosicionHabilitada d where d.funcionario.funId = '").
                append(funcionario.getFunId()).append("'");   
        //System.out.println("DelQry: " + strQry.toString());
        query = em.createQuery(strQry.toString());         
        query.executeUpdate();
            
        /*for (Long posId : listPosiciones) {
            auditoria.auditarDelete(PosicionHabilitada.TABLE_NAME, funcSesion, posId);
        }*/


    }

    @Override
    public List<PosicionHabilitada> getLista(PosicionHabilitada posicionHabilitada, Integer first, Integer pageSize,
            String sortField, String sortOrder) {

        try {
            Query query = createQueryFilter(posicionHabilitada, sortField, sortOrder);
            if(first!=null && pageSize!=null){
                query.setFirstResult(first).setMaxResults(pageSize);
            }
            return (List<PosicionHabilitada>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }

    }

    private Query createQueryFilter(PosicionHabilitada posicionHabilitada, String sortField, String sortOrder) {

        List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder();
        Map<String, Object> params = new java.util.HashMap<String, Object>();

        if (posicionHabilitada.getFuncionario().getFunId() != null) {
            condiciones.add("t.funcionario.funId = :funcid ");
            params.put("funcid", posicionHabilitada.getFuncionario().getFunId());
        }
        if (posicionHabilitada.getFuncionario().getFunEstado() != null) {
            condiciones.add("t.funcionario.funEstado = :funEstado ");
            params.put("funEstado", posicionHabilitada.getFuncionario().getFunEstado());
        }
        if (posicionHabilitada.getFuncionario().getFunAlias() != null && !"".equalsIgnoreCase(posicionHabilitada.getFuncionario().getFunAlias())) {
            condiciones.add("t.funcionario.funAlias = :funAlias ");
            params.put("funAlias", posicionHabilitada.getFuncionario().getFunAlias().toUpperCase());
        }
        if (posicionHabilitada.getFechaini() != null && posicionHabilitada.getFechafin() != null) {
            condiciones.add("t.phFvencimiento between :fini and :ffin ");
            params.put("fini", posicionHabilitada.getFechaini());
            params.put("ffin", posicionHabilitada.getFechafin());
        }
        if (posicionHabilitada.getFechaini() != null && posicionHabilitada.getFechafin() == null) {
            condiciones.add("t.phFvencimiento >= :fini ");
            params.put("fini", posicionHabilitada.getFechaini());

        }
        if (posicionHabilitada.getFechaini() == null && posicionHabilitada.getFechafin() != null) {
            condiciones.add("t.phFvencimiento <= :ffin ");
            params.put("ffin", posicionHabilitada.getFechafin());
        }
        if (posicionHabilitada.getFuncionario().getFunNombre() != null && !"".equals(posicionHabilitada.getFuncionario().getFunNombre())) {
            condiciones.add("upper(t.funcionario.funNombre) like :funcNom ");
            params.put("funcNom", "%" + posicionHabilitada.getFuncionario().getFunNombre().toUpperCase() + "%");
        }
        
        if (posicionHabilitada.getFuncionario().getFunAlias() != null && !"".equals(posicionHabilitada.getFuncionario().getFunAlias())) {
            condiciones.add("upper(t.funcionario.funAlias) like :funcAlias ");
            params.put("funcAlias", "%" + posicionHabilitada.getFuncionario().getFunAlias().toUpperCase() + "%");
        }

        if (posicionHabilitada.getPosicion().getPosId() != null && !"".equals(posicionHabilitada.getPosicion().getPosId())) {
            condiciones.add("t.posicion.posId = :posicion ");
            params.put("posicion", posicionHabilitada.getPosicion().getPosId());
        }
        if (posicionHabilitada.getPosicion().getPosicionNacional() != null
                && posicionHabilitada.getPosicion().getPosicionNacional().getPnAlias() != null
                && !"".equals(posicionHabilitada.getPosicion().getPosicionNacional().getPnAlias())) {
            condiciones.add("upper(t.posicion.posicionNacional.pnAlias) like :posAlias ");
            params.put("posAlias", "%" + posicionHabilitada.getPosicion().getPosicionNacional().getPnAlias().toUpperCase() + "%");
        }

        if (posicionHabilitada.getPosicion().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("t.posicion.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", posicionHabilitada.getPosicion().getDependencia().getAeropuerto().getRegional().getRegId());
        }

        if (posicionHabilitada.getPosicion().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("t.posicion.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", posicionHabilitada.getPosicion().getDependencia().getAeropuerto().getAeId());
        }

        if (posicionHabilitada.getPosicion().getDependencia().getDepId() != null) {
            condiciones.add("t.posicion.dependencia.depId = :dep ");
            params.put("dep", posicionHabilitada.getPosicion().getDependencia().getDepId());
        }
        if (posicionHabilitada.getPosicion().getDependencia().getDepcategoria() != null && posicionHabilitada.getPosicion().getDependencia().getDepcategoria().getDcId() != null && !"".equals(posicionHabilitada.getPosicion().getDependencia().getDepcategoria().getDcId())) {
            condiciones.add("t.posicion.dependencia.depcategoria.dcId = :depcat ");
            params.put("depcat", posicionHabilitada.getPosicion().getDependencia().getDepcategoria().getDcId());
        }

        if (condiciones.size() > 0) {
            strQry.append("where ");
        }
        for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
            strQry.append(it.next());
            if (it.hasNext()) {
                strQry.append(" and ");
            }
        }

        Query query = em.createQuery("Select count(t) from PosicionHabilitada t " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select t from PosicionHabilitada t ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append("order by t.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);
        return query;
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    
    public void renovar(Funcionario funcionario,Funcionario funcionario_session) {
        Calendar actual = Calendar.getInstance();
        actual.add(Calendar.DATE, 90);
        /*strQry = new StringBuilder();
        strQry.append("delete from PosicionHabilitada d where d.funcionario.funId = '").
                append(funcionario.getFunId()).append("'");   
        //System.out.println("DelQry: " + strQry.toString());*/
        query = em.createQuery("update PosicionHabilitada d set d.phFvencimiento=:fecha where d.funcionario.funId = :funId");
        query.setParameter("funId", funcionario.getFunId());
        query.setParameter("fecha", actual.getTime());
        query.executeUpdate();
        /*PosicionHabilitada ph= new PosicionHabilitada();
        ph.setFuncionario(funcionario);        
        List<PosicionHabilitada> listado = this.getLista(ph, null, null, null, null);
        for(int i=0; i<listado.size(); i++)
        {
            //System.out.println("Funcionario\t"+listado.get(i).getFuncionario().getFunId()+"\tPosicion\t"+listado.get(i).getPosicion().getPosId());
            listado.get(i).setPhFvencimiento(actual.getTime());            
            try {
                this.guardar(listado.get(i), funcionario_session);
            } catch (SQLIntegrityConstraintViolationException ex) {
                Logger.getLogger(PosicionHabilitadaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  */    
        
    }
}
