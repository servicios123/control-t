/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
 
/**
 *
 * @author Viviana
 */
@Local
public interface PosicionService {
    public Posicion guardar(Posicion posicion, Funcionario fun);
    
    public List<Posicion> getLista(Posicion posicion, Integer first, Integer pageSize, 
            String sortField, String sortOrder);
    
    public Long getCount();

    public List<BigDecimal> getListaPosicionesDependencia(Long dependenciaId);
    
    public List<Posicion> getListaPosicionesDependencia(Posicion posicion);

    public Posicion getPosicionByPosNal(Long posNacId, Dependencia dep);
    public Object getPorId(long id);

    public void inactivarPosiciones(Long[] arrayPosiciones, Dependencia dep) ;

    public java.util.List<co.gov.aerocivil.controlt.entities.PosicionNacional> getListaPosicionesDependenciaObject(java.lang.Long dependenciaId);

}
