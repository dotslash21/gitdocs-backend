package dev.arunangshu.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestStatusWrapper {

    private RequestStatus status;
    private String message;
    private Object data;
}
