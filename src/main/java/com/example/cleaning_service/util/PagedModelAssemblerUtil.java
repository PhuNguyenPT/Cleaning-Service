package com.example.cleaning_service.util;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PagedModelAssemblerUtil {
    <ID, T, D extends RepresentationModel<D>, I> void addLinksFromPageToPagedModelByMap(
            Map<ID, Link> idLinkMap,
            Page<T> page,
            PagedModel<D> pagedModel,
            Function<T, ID> entityIdExtractor,
            Function<D, ID> modelIdExtractor,
            Function<T, I> interfaceExtractor,
            Function<I, Link> linkExtractor,
            BiConsumer<D, Link> linkAdder
    );
}
