package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Friendship;
import com.potato.petpotatocommunity.entity.FriendshipId;
import com.potato.petpotatocommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {
    List<Friendship> findAllByUser(User user);

    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE f.user.userId = :userId AND f.friend.userId = :friendId")
    boolean existsByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query("SELECT f FROM Friendship f JOIN FETCH f.friend WHERE f.user = :user")
    List<Friendship> findWithFriendByUser(@Param("user") User user);

    boolean existsById(FriendshipId id);
    void deleteById(FriendshipId id);

}
