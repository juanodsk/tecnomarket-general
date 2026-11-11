package com.tecmarket.backend.auth.repository;

import com.tecmarket.backend.auth.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT token FROM RefreshToken token WHERE token.tokenHash = :tokenHash")
    Optional<RefreshToken> findForUpdateByTokenHash(@Param("tokenHash") String tokenHash);

    @Modifying
    @Query("""
            UPDATE RefreshToken token
            SET token.revoked = true,
                token.revokedAt = :revokedAt
            WHERE token.familyId = :familyId
              AND token.revoked = false
            """)
    int revokeActiveFamily(
            @Param("familyId") UUID familyId,
            @Param("revokedAt") LocalDateTime revokedAt);

    @Modifying
    @Query("""
            UPDATE RefreshToken token
            SET token.revoked = true,
                token.revokedAt = :revokedAt
            WHERE token.user.id = :userId
              AND token.revoked = false
            """)
    int revokeAllByUserId(
            @Param("userId") Integer userId,
            @Param("revokedAt") LocalDateTime revokedAt);

    List<RefreshToken> findByUserIdAndRevokedFalseAndExpiresAtAfterOrderByCreatedAtAsc(
            Integer userId,
            LocalDateTime now);

    boolean existsByFamilyIdAndUserIdAndRevokedFalseAndExpiresAtAfter(
            UUID familyId,
            Integer userId,
            LocalDateTime now);

    @Modifying
    @Query("""
            UPDATE RefreshToken token
            SET token.revoked = true,
                token.revokedAt = :revokedAt
            WHERE token.familyId = :familyId
              AND token.user.id = :userId
              AND token.revoked = false
            """)
    int revokeActiveFamilyForUser(
            @Param("familyId") UUID familyId,
            @Param("userId") Integer userId,
            @Param("revokedAt") LocalDateTime revokedAt);

    @Modifying
    @Query("""
            DELETE FROM RefreshToken token
            WHERE token.expiresAt < :cutoff
               OR (token.revoked = true AND token.revokedAt IS NOT NULL AND token.revokedAt < :cutoff)
            """)
    int deleteExpiredAndOldRevoked(@Param("cutoff") LocalDateTime cutoff);
}
