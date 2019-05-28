/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Transporte;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.swing.SortOrder;

/**
 *
 * @author Administrador
 */
@Local
public interface TransporteFuncionarioService {

    Transporte guardar(Transporte transporte, Funcionario f);
    public void borrar(Transporte transporte);
    public Transporte obtenerObjeto(Funcionario funcionario, Date fecha, Jornada jornada);

    public List<Transporte> getLista(Transporte transporteFiltro,
            Integer first, Integer pageSize, String sortField, 
        String sortOrder);

    public Long getCount();
    
}
