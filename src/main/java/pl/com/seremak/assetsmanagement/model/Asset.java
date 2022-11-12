package pl.com.seremak.assetsmanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public abstract class Asset extends VersionedEntity {

    private String userName;
    private String name;
    private BigDecimal value;
}
