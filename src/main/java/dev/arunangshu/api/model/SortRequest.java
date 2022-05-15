package dev.arunangshu.api.model;

import io.quarkus.panache.common.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortRequest {

    public static final Sort.Direction DEFAULT_ORDER = Sort.Direction.Ascending;

    @QueryParam("sort")
    private String sort;

    @Builder.Default
    @QueryParam("order")
    private Sort.Direction order = DEFAULT_ORDER;
}
