package pl.com.seremak.assetsmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Asset extends VersionedEntity {

    private String username;
    private String name;
    private BigDecimal value;
}
