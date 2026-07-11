package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByCode(String code);

    @Query(value = "SELECT r.code FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId AND ur.is_deleted = false AND r.is_deleted = false", nativeQuery = true)
    List<String> findRoleCodesByUserId(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_roles (id, user_id, role_id, created_at, updated_at, is_active, is_deleted, version) VALUES (:id, :userId, :roleId, now(), now(), true, false, 0)", nativeQuery = true)
    void assignRoleToUser(@Param("id") UUID id, @Param("userId") UUID userId, @Param("roleId") UUID roleId);
}
