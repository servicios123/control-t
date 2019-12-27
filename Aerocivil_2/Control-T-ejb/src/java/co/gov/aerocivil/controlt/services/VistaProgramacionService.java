/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface VistaProgramacionService {
     
    public List<Vistaprogramacion> getLista(Vistaprogramacion vistaProgramacion, int first, int pageSize, 
            String sortField, String sortOrder);
    public Long validarCambio(Long TurnoId, Long nuevoFuncionario, Long DepId, boolean jornadaExtra);
    public Long getCount(); 
    public Long validarPosicionNoAsignada(Long funcId, Long depId,Long posId, Long jornId, Date fechaTurno);
    public void eliminarPosNoAsignada(PosNoAsig pos);
    public Long validarAsignarEspecial(Long funcId,  Date fechaTurno, Long turnoEspecial);
}
