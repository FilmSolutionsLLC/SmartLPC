package com.fps.config.util;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

/**
 * Created by macbookpro on 12/11/16.
 */
@Component
public class CurrentTenantIdentifierResolverImpl  implements CurrentTenantIdentifierResolver {

    public CurrentTenantIdentifierResolverImpl() {
        // TODO Auto-generated constructor stub
    }

    private static String tenantValue;
    private static final String DEFAULT_TENANT_ID = "master";

    public void setTenant(String tenant){
        tenantValue = tenant;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        if (tenantValue != null) {
            return tenantValue;
        }
        return DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        // TODO Auto-generated method stub
        return true;
    }
}
