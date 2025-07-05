package vn.com.mbbank.kanban.mbamt.server.service.impl; // Đặt implementation trong package 'impl'

import vn.com.mbbank.kanban.mbamt.server.entity.SysUserEntity;
import vn.com.mbbank.kanban.mbamt.server.repository.SysUserRepository;
import vn.com.mbbank.kanban.mbamt.server.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service implementation for managing SysUsers.
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository sysUserRepository; // Khai báo UserRepository

    @Override
    @Transactional(readOnly = true) // Đánh dấu giao dịch chỉ đọc vì không có thao tác ghi dữ liệu
    public Optional<SysUserEntity> findByUsername(String username) {
        // Kiểm tra đầu vào
        if (username == null || username.trim().isEmpty()) {
            // Có thể ném một BusinessException tùy chỉnh ở đây nếu username không hợp lệ
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return sysUserRepository.findByUsername(username);
    }
}