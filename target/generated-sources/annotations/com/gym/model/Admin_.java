package com.gym.model;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-10-27T10:57:22", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Admin.class)
public class Admin_ { 

    public static volatile SingularAttribute<Admin, String> note;
    public static volatile SingularAttribute<Admin, Date> lastLogin;
    public static volatile SingularAttribute<Admin, Boolean> emailVerified;
    public static volatile SingularAttribute<Admin, Date> createdDate;
    public static volatile SingularAttribute<Admin, String> phone;
    public static volatile SingularAttribute<Admin, Date> lockedUntil;
    public static volatile SingularAttribute<Admin, String> name;
    public static volatile SingularAttribute<Admin, Long> id;
    public static volatile SingularAttribute<Admin, Integer> failedLoginAttempts;
    public static volatile SingularAttribute<Admin, String> passwordHash;
    public static volatile SingularAttribute<Admin, String> email;
    public static volatile SingularAttribute<Admin, String> status;

}