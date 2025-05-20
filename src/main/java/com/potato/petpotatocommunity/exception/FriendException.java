package com.potato.petpotatocommunity.exception;

public class FriendException extends RuntimeException {

    public static class SelfFriendshipNotAllowed extends BusinessException {
        // 자기 자신과 친구 관계를 맺을 수 없을 때 사용하는 정적 팩토리 메서드
        public SelfFriendshipNotAllowed() {
            super("자기 자신은 친구로 추가/삭제할 수 없습니다.", "SELF_FRIENDSHIP_NOT_ALLOWED");
        }
    }

    public static class AlreadyExists extends BusinessException {
        // 자기 자신과 친구 관계를 맺을 수 없을 때 사용하는 정적 팩토리 메서드
        public AlreadyExists() {
            super("이미 친구 관계입니다.", "FRIENDSHIP_ALREADY_EXISTS");
        }
    }

    public static class NotFound extends BusinessException {
        // 자기 자신과 친구 관계를 맺을 수 없을 때 사용하는 정적 팩토리 메서드
        public NotFound() {
            super("해당 사용자와 친구 관계가 아닙니다.", "FRIENDSHIP_NOT_FOUND");
        }
    }
}
