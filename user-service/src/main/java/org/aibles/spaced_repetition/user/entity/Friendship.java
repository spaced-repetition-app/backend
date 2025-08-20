package org.aibles.spaced_repetition.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.spaced_repetition.shared.entity.BaseEntity;
import org.aibles.spaced_repetition.shared.enums.FriendshipStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship extends BaseEntity {
    @Column(nullable = false, length = 36)
    private String userId;

    @Column(nullable = false, length = 36)
    private String friendId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status = FriendshipStatus.PENDING;
}