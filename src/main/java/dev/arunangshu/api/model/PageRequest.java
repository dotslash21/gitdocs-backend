package dev.arunangshu.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequest {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;

    @Builder.Default
    @QueryParam("page")
    private int page = DEFAULT_PAGE;

    @Builder.Default
    @QueryParam("size")
    private int size = DEFAULT_PAGE_SIZE;
}
