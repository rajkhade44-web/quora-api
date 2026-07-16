package quora_api.security.repoository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import quora_api.security.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,UUID> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("Update RefreshToken rt SET rt.revoked = true WHERE rt.familyId = :familyId")
    void revokeAllByFamilyId(@Param("familyId") UUID familyId);

}
