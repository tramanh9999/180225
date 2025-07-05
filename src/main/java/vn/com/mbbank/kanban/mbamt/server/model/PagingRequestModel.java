package vn.com.mbbank.kanban.mbamt.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingRequestModel {
    private int page;
    private int size;
    private boolean isReverse;
    private String sortBy;
}