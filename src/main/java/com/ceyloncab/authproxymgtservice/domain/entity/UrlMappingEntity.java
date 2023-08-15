package com.ceyloncab.authproxymgtservice.domain.entity;

import com.ceyloncab.authproxymgtservice.domain.utils.HttpMethods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "UrlMapping")
@CompoundIndexes({
        @CompoundIndex(name = "exposeUrl_httpMethod_haspathParams_unique_idx", def = "{'exposeUrl' : 1, 'httpMethod' : 1, 'hasPathParams':1}", unique = true),
        @CompoundIndex(def = "{'exposeUrl':1, 'httpMethod':1, 'hasPathParams':1 }", name = "exposeUrl_httpMethod")
})
public class UrlMappingEntity {
    @Id
    private String id;
    private String exposeUrl;
    private String internalUrl;
    private HttpMethods httpMethod;
    private List<String> accessibleRoles;
    private String internalMicroservice;
    private Boolean isDeleted;
    private Boolean hasPathParams;
    @LastModifiedDate
    private Date updatedTime;

}
