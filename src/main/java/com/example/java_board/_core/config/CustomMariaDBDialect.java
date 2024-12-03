package com.example.java_board._core.config;

import java.sql.Types;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;
import org.hibernate.type.descriptor.sql.internal.DdlTypeImpl;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;

public class CustomMariaDBDialect extends MariaDBDialect {

    public CustomMariaDBDialect() {
        super();
    }

    @Override
    protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.registerColumnTypes(typeContributions, serviceRegistry);
        final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();

        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(Types.CLOB, "TEXT", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(Types.BLOB, "BLOB", this));
    }

}