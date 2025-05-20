package com.potato.petpotatocommunity.exception;

public class PetException extends RuntimeException {
    public static class PetNotFoundException extends BusinessException {
        public PetNotFoundException(Long petId) {
            super("PET_NOT_FOUND", String.format("ID가 %d인 반려동물을 찾을 수 없습니다.", petId));
        }
    }
}