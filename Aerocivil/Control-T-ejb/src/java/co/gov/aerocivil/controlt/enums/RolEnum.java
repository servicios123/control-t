/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.enums;

/**
 *
 * @author Viviana
 */
public enum RolEnum {

    NIVEL_SEG(0L, "Administrador de Seguridad de Usuarios"),
    NIVEL_A1(1L, "Líder técnico"),
    NIVEL_A2(2L, "Administrador Nivel 2"),//Regional
    NIVEL_A3(3L, "Administrador Nivel 3"),
    NIVEL_A4(4L, "Administrador Nivel 4"),
    NIVEL_U1(5L, "Usuario Nivel 1"),
    NIVEL_U2(6L, "Usuario Nivel 2"),
    LINEA_3000(7L, "Usuario Línea 3000"),
    AUDITORIA(8L, "Usuario Consulta");
    private Long rolId;

    
    private String rolNombre;

    RolEnum(Long rolId, String rolNombre) {
        this.rolId = rolId;
        this.rolNombre = rolNombre;
    }

    public Long getRolId() {
        return rolId;
    }

    public static RolEnum getRol(Long rolId) {
        for (RolEnum r : RolEnum.values()) {
            if (r.getRolId().equals(rolId)) {
                return r;
            }
        }
        return null;
    }

    public String getRolNombre() {
        return rolNombre;
    }
    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
}
