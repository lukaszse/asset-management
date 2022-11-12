package pl.com.seremak.assetsmanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class VersionedEntity {

    private Metadata metadata;
}
