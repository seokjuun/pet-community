// HTML 이스케이프 처리 함수
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 친구 관계 기간 포맷팅 함수
function formatDuration(createdAt) {
    const now = new Date();
    const then = new Date(createdAt);
    let years = now.getFullYear() - then.getFullYear();
    let months = now.getMonth() - then.getMonth();

    if (months < 0) {
        years--;
        months += 12;
    }

    const parts = [];
    if (years > 0) parts.push(`${years}년`);
    if (months > 0) parts.push(`${months}개월`);

    if (parts.length === 0) {
        // 일 차이 계산
        const nowDateOnly = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const thenDateOnly = new Date(then.getFullYear(), then.getMonth(), then.getDate());
        const diffTime = nowDateOnly - thenDateOnly;
        const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24)) + 1;
        parts.push(`${diffDays}일`);
    }

    return parts.join(' ');
}

// 시간 포맷팅 함수
function formatTime(dateTimeStr) {
    const dt = new Date(dateTimeStr);
    const hh = dt.getHours().toString().padStart(2, '0');
    const mm = dt.getMinutes().toString().padStart(2, '0');
    return `${hh}:${mm}`;
}