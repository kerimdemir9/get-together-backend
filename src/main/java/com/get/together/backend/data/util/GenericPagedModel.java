package com.get.together.backend.data.util;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
@Builder
@Data
public class GenericPagedModel<T> {
    Long totalElements;
    Integer totalPages;
    Integer numberOfElements;
    Collection<T> content;
}
