const scheduleModal = document.getElementById('scheduleModal');
const friendModal = document.getElementById('friendModal');
const scheduleDetailModal = document.getElementById('scheduleDetailModal');

// 일정 추가 모달
document.getElementById('addScheduleBtn').addEventListener('click', function() {
    scheduleModal.style.display = 'block';
});

document.getElementById('closeScheduleModal').addEventListener('click', function() {
    scheduleModal.style.display = 'none';
});

document.getElementById('cancelScheduleBtn').addEventListener('click', function() {
    scheduleModal.style.display = 'none';
});

document.getElementById('saveScheduleBtn').addEventListener('click', function() {
    saveSchedule();
});

// 친구 추가 모달
document.getElementById('addFriendBtn').addEventListener('click', function() {
    friendModal.style.display = 'block';
});

document.getElementById('closeFriendModal').addEventListener('click', function() {
    clearFriendResult();
    friendModal.style.display = 'none';
});

document.getElementById('cancelFriendBtn').addEventListener('click', function() {
    clearFriendResult();
    friendModal.style.display = 'none';
});

document.getElementById('friendSearchBtn').addEventListener('click', function() {
    handleFriendSearch();
});

// 일정 상세 모달
document.getElementById('scheduleDetailCloseBtn').addEventListener('click', function() {
    closeModal();
});

document.getElementById('scheduleDeleteBtn').addEventListener('click', function() {
    deleteSchedule();
});

// 모달 외부 클릭 시 닫기
window.addEventListener('click', function(event) {
    if (event.target === scheduleModal) {
        scheduleModal.style.display = 'none';
    }
    if (event.target === friendModal) {
        clearFriendResult();
        friendModal.style.display = 'none';
    }
    if (event.target === scheduleDetailModal) {
        closeModal();
    }
});