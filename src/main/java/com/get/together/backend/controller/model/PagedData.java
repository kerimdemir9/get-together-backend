package com.get.together.backend.controller.model;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedData<T> {
    Long totalElements;
    Integer totalPages;
    Integer numberOfElements;
    Collection<T> content;
}
