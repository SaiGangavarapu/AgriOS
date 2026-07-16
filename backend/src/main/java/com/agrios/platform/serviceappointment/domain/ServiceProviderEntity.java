package com.agrios.platform.serviceappointment.domain;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant; import java.util.UUID; import org.hibernate.annotations.JdbcTypeCode; import org.hibernate.type.SqlTypes;
@Entity @Table(name="service_provider",schema="service")
public class ServiceProviderEntity {
 @Id private UUID id; @Column(nullable=false) private UUID tenantId; private UUID expertProfileId; private UUID userAccountId;
 @Column(nullable=false) private String providerType; @Column(nullable=false) private String displayName; private String organizationName; private String phoneNumber; private String emailAddress; private String registrationReference;
 @JdbcTypeCode(SqlTypes.JSON) @Column(nullable=false,columnDefinition="jsonb") private String serviceAreaCodes;
 @JdbcTypeCode(SqlTypes.JSON) @Column(nullable=false,columnDefinition="jsonb") private String languageCodes;
 @JdbcTypeCode(SqlTypes.JSON) @Column(nullable=false,columnDefinition="jsonb") private String serviceCodes;
 @Column(nullable=false) private String consultationMode; @Column(nullable=false) private String status; @Column(nullable=false) private BigDecimal averageRating; @Column(nullable=false) private int reviewCount; @Version private long version; @Column(nullable=false) private Instant createdAt; @Column(nullable=false) private Instant updatedAt;
 protected ServiceProviderEntity(){}
 public static ServiceProviderEntity create(UUID tenantId,UUID expertProfileId,UUID userAccountId,String providerType,String displayName,String organizationName,String phone,String email,String registration,String areas,String languages,String services,String mode){var v=new ServiceProviderEntity();v.id=UUID.randomUUID();v.tenantId=tenantId;v.expertProfileId=expertProfileId;v.userAccountId=userAccountId;v.providerType=providerType;v.displayName=displayName;v.organizationName=organizationName;v.phoneNumber=phone;v.emailAddress=email;v.registrationReference=registration;v.serviceAreaCodes=areas;v.languageCodes=languages;v.serviceCodes=services;v.consultationMode=mode;v.status="ACTIVE";v.averageRating=BigDecimal.ZERO;v.createdAt=Instant.now();v.updatedAt=v.createdAt;return v;}
 public void refreshRating(double avg,int count){averageRating=BigDecimal.valueOf(avg);reviewCount=count;updatedAt=Instant.now();}
 public UUID id(){return id;} public UUID tenantId(){return tenantId;} public String providerType(){return providerType;} public String displayName(){return displayName;} public String organizationName(){return organizationName;} public String consultationMode(){return consultationMode;} public String status(){return status;} public double averageRating(){return averageRating.doubleValue();} public int reviewCount(){return reviewCount;}
}
