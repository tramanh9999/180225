package com.example.demo.repository;

import com.example.demo.enums.ChangeFlowNodeType;
import com.example.demo.model.ChangeFlowNodeModel;
import com.example.demo.model.PagingRequestModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ChangeFlowNodeRepositoryCustomImpl implements ChangeFlowNodeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<String> findUsernamesByChangeFlowNodeId(Long changeFlowNodeId,
                                                        PagingRequestModel requestModel) {
        if (changeFlowNodeId == null) {
            throw new IllegalArgumentException("Change Flow Node ID cannot be null.");
        }
        if (requestModel.getPage() < 0) {
            throw new IllegalArgumentException("Page number cannot be negative.");
        }
        if (requestModel.getSize() <= 0) {
            throw new IllegalArgumentException("Page size must be positive.");
        }

        // --- 1. Thực hiện truy vấn COUNT để lấy tổng số bản ghi ---
        long totalElements = countUsersByChangeFlowNodeIdNative(changeFlowNodeId);

        // Nếu không có bản ghi nào, trả về Page rỗng ngay
        if (totalElements == 0) {
            // Sử dụng PageRequest.of để tạo Pageable cho PageImpl
            return new PageImpl<>(List.of(),
                    PageRequest.of(requestModel.getPage(), requestModel.getSize()), 0);
        }

        // --- 2. Thực hiện truy vấn lấy dữ liệu có phân trang ---
        // Tạo đối tượng Pageable từ page và size
        Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getSize());

        String dataSql = """
                SELECT su.USERNAME
                FROM SYS_USER su
                JOIN SYS_USER_GROUP sug ON su.USERNAME = sug.USERNAME
                JOIN SYS_GROUP sg ON sug.GROUP_ID = sg.ID
                JOIN CHANGE_FLOW_NODE_GROUP cfng ON sg.ID = cfng.GROUP_ID
                JOIN CHANGE_FLOW_NODE cfn ON cfng.CHANGE_FLOW_NODE_ID = cfn.ID
                WHERE cfn.ID = :nodeId
                ORDER BY su.USERNAME ASC
                OFFSET :offset ROWS
                FETCH NEXT :size ROWS ONLY
                """;
        // OFFSET và FETCH NEXT được sử dụng trong Native Query
        // Bạn cũng có thể dùng setFirstResult() và setMaxResults() của JPA Query

        Query dataQuery = entityManager.createNativeQuery(dataSql);
        dataQuery.setParameter("nodeId", changeFlowNodeId);
        // Sử dụng thông tin từ Pageable để thiết lập offset và limit cho Native Query
        dataQuery.setParameter("offset", pageable.getOffset());
        dataQuery.setParameter("size", pageable.getPageSize());

        List<Object> results = dataQuery.getResultList();
        List<String> usernames =
                results.stream().map(Object::toString).collect(Collectors.toList());

        // --- 3. Trả về đối tượng org.springframework.data.domain.Page ---
        // Sử dụng PageImpl để tạo Page object
        return new PageImpl<>(usernames, pageable, totalElements);
    }


    @Override
    public long countUsersByChangeFlowNodeIdNative(Long changeFlowNodeId) {
        if (changeFlowNodeId == null) {
            throw new IllegalArgumentException("Change Flow Node ID cannot be null");
        }

        String sql = """
                SELECT COUNT(DISTINCT su.ID)
                FROM SYS_USER su
                JOIN SYS_USER_GROUP sug ON su.USERNAME = sug.USERNAME
                JOIN SYS_GROUP sg ON sug.GROUP_ID = sg.ID
                JOIN CHANGE_FLOW_NODE_GROUP cfng ON sg.ID = cfng.GROUP_ID
                JOIN CHANGE_FLOW_NODE cfn ON cfng.CHANGE_FLOW_NODE_ID = cfn.ID
                WHERE cfn.ID = :nodeId
                """;

        Object result =
                entityManager.createNativeQuery(sql).setParameter("nodeId", changeFlowNodeId)
                        .getSingleResult();

        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        return 0;
    }


    /**
     * Returns a list of change flow nodes for a given change template ID.
     * Nodes are ordered by their level in ascending order.
     *
     * @param changeTemplateId the ID of the change template
     * @return list of ChangeFlowNodeModel objects
     */


    @Override
    public List<ChangeFlowNodeModel> findChangeFlowNodesByTemplateId(Long changeTemplateId) {
        // Use Objects.requireNonNull for immediate validation, or throw IllegalArgumentException
        Objects.requireNonNull(changeTemplateId, "Change Template ID cannot be null");
        // Or your original validation:
        // if (changeTemplateId == null) {
        //     throw new IllegalArgumentException("Change Template ID cannot be null");
        // }

        // Use Text Blocks (Java 15+) for multi-line SQL for readability.
        // If using older Java, concatenate strings or use a separate SQL file.
        String sql = """
                SELECT
                    flowNode.ID,
                    flowNode.NAME,
                    flowNode.CHANGE_FLOW_ID,
                    flowNode.TYPE,
                    flowNode.NODE_LEVEL
                FROM
                    CHANGE_TEMPLATE changeTemplate
                    JOIN CHANGE_FLOW changeFlow ON changeTemplate.CHANGE_FLOW_ID = changeFlow.ID
                    JOIN CHANGE_FLOW_NODE flowNode ON changeFlow.ID = flowNode.CHANGE_FLOW_ID
                WHERE
                    changeTemplate.ID = :changeTemplateId
                
                ORDER BY
                    flowNode.NODE_LEVEL ASC
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("changeTemplateId", changeTemplateId);

        // Get the result list. createNativeQuery returns List<Object[]>.
        List<Object[]> results = query.getResultList();

        // Handle empty results gracefully
        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        // Map the Object[] results to ChangeFlowNodeModel using the builder pattern
        return results.stream().map(row -> ChangeFlowNodeModel.builder()
                .id(((Number) row[0]).longValue())         // Assuming ID is first column and a Number type
                .name((String) row[1])                     // Assuming NAME is second column and a String
                .changeFlowId(
                        ((Number) row[2]).longValue()) // Assuming CHANGE_FLOW_ID is third and Number
                .type((ChangeFlowNodeType) ChangeFlowNodeType.valueOf(
                        String.valueOf(row[3])))                    // Assuming
                // TYPE
                // is fourth
                // and String
                .nodeLevel(
                        ((Number) row[4]).intValue())   // Assuming NODE_LEVEL is fifth and Number
                .build()).collect(Collectors.toList());
    }


}