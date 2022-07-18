package org.athena.storage.business;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.athena.common.exception.EntityAlreadyExistsException;
import org.athena.common.resp.PageResp;
import org.athena.common.util.SnowflakeIdWorker;
import org.athena.storage.db.StoreSpacesRepository;
import org.athena.storage.db.queries.StoreSpacesQueries;
import org.athena.storage.entity.StoreSpace;
import org.athena.storage.resp.StoreSpaceResp;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 存储空间业务处理类
 */
@Singleton
public class StoreSpacesBusiness {

    @Inject
    private SnowflakeIdWorker idWorker;

    @Inject
    private StoreSpacesQueries storeSpacesQueries;

    @Inject
    private StoreSpacesRepository storeSpacesRepository;

    @Inject
    private AthenaFileBusiness athenaFileBusiness;

    /**
     * 创建存储空间
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void create(Long userId, String name, String description) {
        Optional<StoreSpace> optional = storeSpacesRepository.findByName(name);
        if (optional.isPresent()) {
            throw EntityAlreadyExistsException.build("存储空间已存在，请重试");
        }
        StoreSpace storeSpace = StoreSpace.builder().creatorId(userId).name(name).description(description)
                .size(0L).deleted(Boolean.FALSE).storeSpaceId(idWorker.nextId())
                .createTime(Instant.now()).build();
        storeSpacesRepository.save(storeSpace);
        // 创建存储空间对应的祖先文件目录
        athenaFileBusiness.createAncestorFile(userId, storeSpace.getStoreSpaceId(), storeSpace.getName());
    }

    /**
     * 获取存储空间
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public PageResp<StoreSpaceResp> find(Long userId, String name, Long limit, Long offset) {
        List<StoreSpace> list = storeSpacesQueries.findByCreatorIdAndNameLike(userId, name, limit, offset);
        if (list.isEmpty()) {
            return PageResp.of(Lists.newArrayList(), limit, offset);
        }
        Long total = storeSpacesQueries.countByCreatorIdAndNameLike(userId, name);
        return PageResp.of(list.stream().map(x -> StoreSpaceResp.builder().storeSpacesId(x.getStoreSpaceId())
                        .name(x.getName()).size(x.getSize()).creatorId(x.getCreatorId())
                        .description(x.getDescription()).deleted(x.getDeleted()).createTime(x.getCreateTime())
                        .build()).collect(Collectors.toList()),
                limit, offset, total);
    }

    /**
     * 更新存储空间备注
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void update(Long userId, Long storeSpaceId, String description) {
        Optional<StoreSpace> optional = storeSpacesRepository.findByStoreSpaceIdAndCreatorId(userId, storeSpaceId);
        if (!optional.isPresent()) {
            throw EntityAlreadyExistsException.build("此用户下不存在该存储空间，请重试");
        }
        StoreSpace storeSpace = optional.get();
        storeSpace.setDescription(description);
        storeSpace.setModifyTime(Instant.now());
        storeSpacesRepository.update(storeSpace);
    }

    /**
     * 获取存储空间详情
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public StoreSpaceResp get(Long userId, Long storeSpaceId) {
        StoreSpace storeSpace = this.getStoreSpace(userId, storeSpaceId);
        return StoreSpaceResp.builder().storeSpacesId(storeSpace.getStoreSpaceId())
                .name(storeSpace.getName()).size(storeSpace.getSize()).creatorId(storeSpace.getCreatorId())
                .description(storeSpace.getDescription()).deleted(storeSpace.getDeleted())
                .createTime(storeSpace.getCreateTime()).build();
    }

    /**
     * 获取存储空间详情
     */
    StoreSpace getStoreSpace(Long userId, Long storeSpaceId) {
        Optional<StoreSpace> optional = storeSpacesRepository.findByStoreSpaceIdAndCreatorId(storeSpaceId, userId);
        if (!optional.isPresent()) {
            throw EntityAlreadyExistsException.build("此用户下不存在该存储空间，请重试");
        }
        return optional.get();
    }

}
