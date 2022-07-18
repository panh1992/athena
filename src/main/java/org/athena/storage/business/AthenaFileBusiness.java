package org.athena.storage.business;

import com.google.inject.Singleton;
import org.athena.common.exception.EntityAlreadyExistsException;
import org.athena.common.exception.EntityNotExistException;
import org.athena.common.exception.InternalServerError;
import org.athena.common.exception.InvalidParameterException;
import org.athena.common.resp.PageResp;
import org.athena.common.util.PathUtil;
import org.athena.common.util.SnowflakeIdWorker;
import org.athena.storage.db.AthenaFileRepository;
import org.athena.storage.db.PathTreeRepository;
import org.athena.storage.db.StoreSpacesRepository;
import org.athena.storage.entity.AthenaFile;
import org.athena.storage.entity.StoreSpace;
import org.athena.storage.resp.FileInfoResp;
import org.athena.storage.resp.FileResp;
import org.athena.storage.sdk.util.FileStatus;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文件业务处理类
 */
@Singleton
public class AthenaFileBusiness {

    private static final Logger logger = LoggerFactory.getLogger(AthenaFileBusiness.class);

    @Inject
    private SnowflakeIdWorker idWorker;

    @Inject
    private StoreSpacesBusiness storeSpacesBusiness;

    @Inject
    private StoreSpacesRepository storeSpacesRepository;

    @Inject
    private AthenaFileRepository athenaFileRepository;

    @Inject
    private PathTreeRepository pathTreeRepository;

    /**
     * 获取下级文件列表
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public PageResp findNextAll(Long userId, Long storeSpaceId, Long fileId, Long limit, Long offset) {

        Optional<StoreSpace> optionalStoreSpace = storeSpacesRepository
                .findByStoreSpaceIdAndCreatorId(storeSpaceId, userId);
        if (!optionalStoreSpace.isPresent()) {
            throw EntityNotExistException.build("不存在此存储空间, 请校验");
        }
        StoreSpace storeSpace = storeSpacesBusiness.getStoreSpace(userId, storeSpaceId);
        if (Objects.isNull(fileId)) {
            AthenaFile ancestorFile = this.getAncestorFile(storeSpace.getStoreSpaceId(), storeSpace.getName());
            fileId = ancestorFile.getFileId();
        }
        List<AthenaFile> files = athenaFileRepository.findByDescendantFileAndDepth(fileId, 1L, limit, offset);
        Long total = athenaFileRepository.countByDescendantFileAndDepth(fileId, 1L);
        return PageResp.of(files.stream().map(x -> FileResp.builder().fileId(x.getFileId()).fileName(x.getFileName())
                        .fileSize(x.getFileSize()).format(x.getFormat()).storeSpace(x.getStoreSpaceName())
                        .status(x.getStatus()).isDir(x.getDir()).checkSum(x.getCheckSum())
                        .description(x.getDescription()).createTime(x.getCreateTime()).build())
                        .collect(Collectors.toList()),
                limit, offset, total);
    }

    /**
     * 获取用户下文件详细信息
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public FileResp get(Long userId, Long fileId) {

        AthenaFile file = this.getAthenaFile(userId, fileId);

        return FileInfoResp.infoBuilder().fileId(file.getFileId()).fileName(file.getFileName())
                .fileSize(file.getFileSize()).isDir(file.getDir()).status(file.getStatus())
                .filePath(athenaFileRepository.findFilePath(file.getStoreSpaceId(), file.getFileId()))
                .checkSum(file.getCheckSum()).format(file.getFormat()).createTime(file.getCreateTime())
                .description(file.getDescription()).storeSpace(file.getStoreSpaceName()).build();
    }

    /**
     * 创建文件元数据
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ)
    public void create(Long userId, Long storeSpaceId, String filePath, boolean isDir, String description) {
        if (!PathUtil.isPath(filePath, isDir)) {
            throw InvalidParameterException.build("文件路径不合法, 请检查");
        }
        Optional<StoreSpace> optionalStoreSpace = storeSpacesRepository
                .findByStoreSpaceIdAndCreatorId(storeSpaceId, userId);
        if (!optionalStoreSpace.isPresent()) {
            throw EntityNotExistException.build("存储空间不存在");
        }
        List<String> fileNames = PathUtil.getFileNames(filePath);
        Optional<AthenaFile> checkFile = athenaFileRepository.findByStoreSpaceIdAndDescendantFileNameAndDepth(
                storeSpaceId, fileNames.get(fileNames.size() - 1), fileNames.size() + 1);
        if (checkFile.isPresent()) {
            throw EntityAlreadyExistsException.build("文件已存在, 请校验");
        }
        StoreSpace storeSpace = optionalStoreSpace.get();
        AthenaFile athenaFile = this.getAncestorFile(storeSpace.getStoreSpaceId(), storeSpace.getName());
        Long ancestorFileId = athenaFile.getFileId();
        for (int depth = 1; depth < fileNames.size(); depth++) {
            String fileName = fileNames.get(depth);
            // 如果文件存在跳过, 不存在创建
            Optional<AthenaFile> optionalFile = athenaFileRepository
                    .findByStoreSpaceIdAndDescendantFileNameAndDepth(storeSpaceId, fileName, depth);
            if (optionalFile.isPresent()) {
                ancestorFileId = optionalFile.get().getFileId();
                continue;
            }
            boolean dir = (depth + 1) != fileNames.size() || isDir;
            AthenaFile file = AthenaFile.builder().fileId(idWorker.nextId()).fileName(fileName).description(description)
                    .storeSpaceId(storeSpace.getStoreSpaceId()).storeSpaceName(storeSpace.getName())
                    .status(dir ? FileStatus.AVAILABLE.name() : FileStatus.NEW.name()).dir(dir).creatorId(userId)
                    .format(dir ? null : PathUtil.getExtName(fileName)).createTime(Instant.now()).build();
            athenaFileRepository.save(file);
            pathTreeRepository.save(ancestorFileId, file.getFileId());
            ancestorFileId = file.getFileId();
        }
    }

    /**
     * 创建祖先文件目录
     */
    void createAncestorFile(Long userId, Long storeSpaceId, String storeSpaceName) {
        AthenaFile ancestorFile = AthenaFile.builder().fileId(idWorker.nextId()).fileName(storeSpaceName)
                .status(FileStatus.AVAILABLE.name()).creatorId(userId).storeSpaceId(storeSpaceId)
                .storeSpaceName(storeSpaceName).description(storeSpaceName + "  存储空间根目录")
                .dir(Boolean.TRUE).createTime(Instant.now()).build();
        athenaFileRepository.save(ancestorFile);
        pathTreeRepository.save(ancestorFile.getFileId(), ancestorFile.getFileId());
    }

    /**
     * 获取存储空间对应的祖先文件
     */
    private AthenaFile getAncestorFile(Long storeSpaceId, String storeSpaceName) {
        Optional<AthenaFile> optionalAncestorFile = athenaFileRepository
                .findByStoreSpaceIdAndFileNameAndDepth(storeSpaceId, storeSpaceName, 0);
        if (!optionalAncestorFile.isPresent()) {
            logger.error("存储空间: {}, 获取祖先文件失败", storeSpaceName);
            throw InternalServerError.build("存储空间: " + storeSpaceName + ", 获取祖先文件失败");
        }
        return optionalAncestorFile.get();
    }

    /**
     * 获取某用户下的文件
     */
    private AthenaFile getAthenaFile(Long userId, Long fileId) {
        Optional<AthenaFile> optionalAthenaFile = athenaFileRepository.findByCreatorIdAndFileId(userId, fileId);
        if (!optionalAthenaFile.isPresent()) {
            throw EntityNotExistException.build("不存在此文件，请校验");
        }
        return optionalAthenaFile.get();
    }

    /**
     * 移动某用户下的文件
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void move(Long userId, Long fileId, Long fileDirId) {
        if (fileId.compareTo(fileDirId) == 0) {
            throw InvalidParameterException.build("要移动文件与目标文件为同一文件，请重试");
        }
        AthenaFile file = this.getAthenaFile(userId, fileId);
        AthenaFile athenaFile = this.getAncestorFile(file.getStoreSpaceId(), file.getStoreSpaceName());
        if (athenaFile.getFileId().equals(file.getFileId())) {
            throw InvalidParameterException.build("根目录不允许移动");
        }
        AthenaFile dir = this.getAthenaFile(userId, fileDirId);
        if (!dir.getDir()) {
            throw InvalidParameterException.build("目标文件不是目录，不允许移动，请校验");
        }
        // TODO: 实现数据修改

    }

    /**
     * 删除某用户下文件
     *
     * @param isDel 是否强制删除
     */
    public void remove(Long userId, Long fileId, Boolean isDel) {
        AthenaFile file = this.getAthenaFile(userId, fileId);
        AthenaFile athenaFile = this.getAncestorFile(file.getStoreSpaceId(), file.getStoreSpaceName());
        if (athenaFile.getFileId().equals(file.getFileId())) {
            throw InvalidParameterException.build("根目录不允许删除");
        }
        // 如果文件为目录且不强制删除, 校验目录下是否有文件
        if (file.getDir() && !isDel && athenaFileRepository.countByDescendantFileAndDepth(fileId, 1L) > 0) {
            throw InvalidParameterException.build(Response.Status.CONFLICT, "删除失败, 该目录下存在文件");
        }
        athenaFileRepository.removeAthenaFile(fileId, userId, FileStatus.DELETED.name());
    }

}
