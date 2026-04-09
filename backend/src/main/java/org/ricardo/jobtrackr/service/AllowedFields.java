package org.ricardo.jobtrackr.service;

import java.math.BigDecimal;
import java.util.Date;

public enum AllowedFields {
    POSTULACION_ID("postulacion_id", Integer.class),
    USUARIO_ID("usuario_id", Integer.class),
    EMPRESA_ID("empresa_id", Integer.class),
    ROL("rol", String.class),
    ESTATUS("estatus", String.class),
    ORDEN_KANBAN("orden_kanban", Integer.class),
    SALARIO_MINIMO("salario_minimo", BigDecimal.class),
    SALARIO_MAXIMO("salario_maximo", BigDecimal.class),
    UBICACION("ubicacion", String.class),
    ES_TELEMATICO("es_telematico", Boolean.class),
    OFERTA_URL("oferta_url", String.class),
    NOTA_POSTULACION("nota_postulacion", String.class),
    FECHA_POSTULACION("fecha_postulacion", Date.class);

    private final String sqlField;
    private final Class<?> fieldType;

    <T> AllowedFields(String sqlField, Class<T> fieldType) {
        this.sqlField = sqlField;
        this.fieldType = fieldType;
    }

    public String getSqlField() {
        return sqlField;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public static AllowedFields fromSqlField(String sqlField) {
        for (AllowedFields field : AllowedFields.values()) {
            if (field.getSqlField().equals(sqlField)) {
                return field;
            }
        }
        return null;
    }
}
