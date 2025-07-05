package vn.com.mbbank.kanban.mbamt.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roleId;
    private String moduleAction;
    private String permissionAction;
    private String type;
    private Long resourceId;
    private String subModuleAction;

    // You will likely need to add other fields here based on your database schema
}
