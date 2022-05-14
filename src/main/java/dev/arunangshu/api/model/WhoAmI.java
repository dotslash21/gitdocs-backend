package dev.arunangshu.api.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RegisterForReflection
public class WhoAmI {

    private String name;
    private List<String> roles;
}
