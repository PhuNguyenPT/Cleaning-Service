package com.example.cleaning_service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
class PagedModelAssemblerUtilImpl implements PagedModelAssemblerUtil{
    private static final Logger log = LoggerFactory.getLogger(PagedModelAssemblerUtilImpl.class);

    @Transactional
    @Override
    public <ID, T, D extends RepresentationModel<D>, I> void addLinksFromPageToPagedModelByMap(
            Map<ID, Link> idLinkMap,
            Page<T> page,
            PagedModel<D> pagedModel,
            Function<T, ID> entityIdExtractor,
            Function<D, ID> modelIdExtractor,
            Function<T, I> interfaceExtractor,
            Function<I, Link> linkExtractor,
            BiConsumer<D, Link> linkAdder) {

        // Populate the map with links
        page.getContent().forEach(entity -> {
            ID id = entityIdExtractor.apply(entity);
            log.info("Extract id: {} from {}", id, entity);

            I interfaceObject = interfaceExtractor.apply(entity);
            if (id != null && interfaceObject != null) {
                log.info("Extract organization: {} from {}", interfaceObject, entity);
                Link link = linkExtractor.apply(interfaceObject);
                log.info("Extract link: {} from {}", link, interfaceObject);
                idLinkMap.put(id, link);
            }
        });

        // Attach links to corresponding response models
        pagedModel.getContent().forEach(responseModel -> {
            ID id = modelIdExtractor.apply(responseModel);
            Link link = idLinkMap.get(id);
            if (link != null) {
                linkAdder.accept(responseModel, link);
                log.info("Add link: {} to {}", link, responseModel);
            }
        });
    }
}
