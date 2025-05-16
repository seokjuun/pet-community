package com.potato.petpotatocommunity.exception;

public class ScheduleException extends RuntimeException {
    public static class ScheduleNotFoundException extends BusinessException {
        public ScheduleNotFoundException(Long scheduleId) {
            super("SCHEDULE_NOT_FOUND", String.format("ID가 %d인 일정을 찾을 수 없습니다.", scheduleId));
        }
    }

    public static class UnauthorizedScheduleAccessException extends BusinessException {
        public UnauthorizedScheduleAccessException(Long userId, Long scheduleId) {
            super("UNAUTHORIZED_SCHEDULE_ACCESS", String.format("사용자 ID %d는 일정 ID %d에 접근할 권한이 없습니다.", userId, scheduleId));
        }
    }

    public static class InvalidScheduleTimeException extends BusinessException {
        public InvalidScheduleTimeException(String message) {
            super("INVALID_SCHEDULE_TIME", message);
        }
    }

    public static class ScheduleOverlapException extends BusinessException {
        public ScheduleOverlapException(String dayOfWeek, String timeSlot) {
            super("SCHEDULE_OVERLAP", String.format("%s요일 %s 시간에 이미 등록된 일정이 있습니다.", dayOfWeek, timeSlot));
        }
    }

    public static class PetNotOwnedException extends BusinessException {
        public PetNotOwnedException(Long userId, Long petId) {
            super("PET_NOT_OWNED", String.format("사용자 ID %d는 반려동물 ID %d의 소유자가 아닙니다.", userId, petId));
        }
    }
}


