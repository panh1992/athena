package org.athena.account.business;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.collections4.CollectionUtils;
import org.athena.account.db.ResourceRepository;
import org.athena.account.db.queries.ResourceQueries;
import org.athena.account.entity.Resource;
import org.athena.account.params.ResourceParams;
import org.athena.account.resp.ResourceResp;
import org.athena.common.exception.EntityNotExistException;
import org.athena.common.resp.PageResp;
import org.athena.common.util.SnowflakeIdWorker;
import org.athena.common.util.crypto.CRC32Util;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ResourceBusiness {

    @Inject
    private SnowflakeIdWorker idWorker;

    @Inject
    private ResourceRepository resourceRepository;

    @Inject
    private ResourceQueries resourceQueries;

    /**
     * 此 uri 和 method 是否需要进行鉴权
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public boolean isVerify(String uri, String method) {
        return this.isVerify(uri, Lists.newArrayList(method));
    }

    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    private boolean isVerify(String uri, List<String> methods) {
        return resourceRepository.exists(uri, methods);
    }

    /**
     * 查询所有资源
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public PageResp<ResourceResp> findAll(String name, String platform, String module, String uri,
                                          String method, Long limit, Long offset) {
        List<Resource> resources = resourceQueries.findAll(name, platform, module, uri, method, limit, offset);
        if (CollectionUtils.isEmpty(resources)) {
            return PageResp.of(Lists.newArrayList(), limit, offset);
        }
        Long total = resourceQueries.countAll(name, platform, module, uri, method);
        return PageResp.of(resources.stream().map(this::build).collect(Collectors.toList()), limit, offset, total);
    }

    /**
     * 创建资源
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void create(ResourceParams params) {
        // TODO: 添加校验
        Resource resource = Resource.builder().resourceId(idWorker.nextId()).name(params.getName()).uri(params.getUri())
                .method(params.getMethod()).module(params.getModule()).createTime(Instant.now())
                .permission(CRC32Util.calculate(params.getUri().concat(",").concat(params.getMethod())))
                .description(params.getDescription()).build();
        resourceRepository.save(resource);
    }

    /**
     * 根据 资源ID 获取资源
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public ResourceResp get(Long resourceId) {
        Resource resource = this.getResource(resourceId);
        return this.build(resource);
    }

    /**
     * 通过 uri 和 method 获取资源
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public Optional<Resource> get(String uri, String method) {
        return resourceRepository.findByUriAndMethod(uri, method);
    }

    /**
     * 根据 资源ID 获取资源
     */
    public Resource getResource(Long resourceId) {
        Optional<Resource> optional = resourceRepository.findByResourceId(resourceId);
        if (!optional.isPresent()) {
            throw EntityNotExistException.build("此资源不存在，请校验");
        }
        return optional.get();
    }

    /**
     * 修改资源信息
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void update(Long resourceId, ResourceParams params) {
        Resource resource = this.getResource(resourceId);
        // TODO: 添加校验
        resource.setName(params.getName());
        resource.setUri(params.getUri());
        resource.setMethod(params.getMethod());
        resource.setModule(params.getModule());
        resource.setPermission(CRC32Util.calculate(params.getUri().concat(",").concat(params.getMethod())));
        resource.setDescription(params.getDescription());
        resource.setModifyTime(Instant.now());
        resourceRepository.update(resource);
    }

    /**
     * 删除资源
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void delete(Long resourceId) {
        Resource resource = this.getResource(resourceId);
        resourceRepository.delete(resource.getResourceId());
    }

    /**
     * 构建返回实体
     */
    private ResourceResp build(Resource resource) {
        return ResourceResp.builder().resourceId(resource.getResourceId()).name(resource.getName())
                .uri(resource.getUri()).method(resource.getMethod()).module(resource.getModule())
                .permission(resource.getPermission()).createTime(Instant.now())
                .modifyTime(resource.getModifyTime()).description(resource.getDescription()).build();
    }

}
