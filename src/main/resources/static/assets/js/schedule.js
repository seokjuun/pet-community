/* 일정 불러오기 */
async function loadSchedules() {
    try {
        const res = await fetch('/schedules');
        const schedules = await res.json();

        renderScheduleGrid(schedules, 'user');
    } catch (e) {
        console.error(e);
        alert('일정 데이터를 불러오는데 실패했습니다.');
    }
}

async function loadFriendSchedulesTable(friendId) {
    try {
        const res = await fetch('/schedules/' + friendId);
        if (!res.ok) throw new Error('일정 정보를 불러오는 데 실패했습니다.');
        const schedules = await res.json();
        renderScheduleGrid(schedules, 'friend');
    } catch (e) {
        console.error(e);
        alert('일정 데이터를 불러오는데 실패했습니다.');
    }
}

function renderScheduleGrid(schedules, status) {
    const timeSlotRowMap = { AM: 0, PM: 1, EVENING: 2 };

    const table = status === 'user'
        ? document.querySelector('.schedule-table')
        : document.querySelector('#friend-schedule-table');

    const tbodyRows = table.querySelectorAll('tbody tr'); // 0=오전,1=오후,2=저녁

    clearScheduleTable(tbodyRows);
    populateScheduleItems(schedules, tbodyRows, timeSlotRowMap, status);
    fillEmptySlots(tbodyRows);
}

function clearScheduleTable(tbodyRows) {
    tbodyRows.forEach(row => {
        row.querySelectorAll('td').forEach((cell, idx) => {
            if (idx > 0) cell.innerHTML = '';
        });
    });
}

function populateScheduleItems(schedules, tbodyRows, timeSlotRowMap, status) {
    schedules.forEach(item => {
        const rowIndex = timeSlotRowMap[item.timeSlot];
        const cell = tbodyRows[rowIndex].children[item.dateOfWeek + 1];

        const div = document.createElement('div');
        div.className = 'schedule-item';
        div.dataset.id = item.scheduleId;
        div.dataset.day = item.dateOfWeek;
        div.dataset.period = item.timeSlot.toLowerCase();
        div.innerHTML = `
        <div class="schedule-item-time">
          ${formatTime(item.startTime)} - ${formatTime(item.endTime)}
        </div>
        <div class="schedule-item-place">${escapeHtml(item.location)}</div>
        <div class="schedule-item-pet">${escapeHtml(item.petName)}</div>
    `;

        // 클릭 시 디테일 열기
        div.addEventListener('click', () => {
            openScheduleDetail(item.scheduleId, status);
        });

        cell.appendChild(div);
    });
}

function fillEmptySlots(tbodyRows) {
    tbodyRows.forEach(row => {
        row.querySelectorAll('td').forEach((cell, idx) => {
            if (idx === 0) return; // 시간대 컬럼 건너뛰기
            if (cell.children.length === 0) {
                const span = document.createElement('span');
                span.className = 'schedule-empty';
                span.textContent = '일정 없음';
                cell.appendChild(span);
            }
        });
    });
}

/* 일정 추가 */
async function saveSchedule() {
    const form = document.getElementById('scheduleForm');

    try {
        const dayOfWeek = parseInt(form.day.value, 10);
        const period    = form.period.value;        // morning|afternoon|evening
        const startTime = form.startTime.value;     // "HH:mm"
        const endTime   = form.endTime.value;
        const location  = form.place.value.trim();
        const petId     = parseInt(form.pet.value, 10);
        const notes     = form.memo.value.trim();

        const slotMap = { morning:'AM', afternoon:'PM', evening:'EVENING' };
        const timeSlot = slotMap[period];

        const today = new Date();
        const yyyy = today.getFullYear();
        const mm   = String(today.getMonth()+1).padStart(2,'0');
        const dd   = String(today.getDate()).padStart(2,'0');
        const startDateTime = `${yyyy}-${mm}-${dd}T${startTime}:00`;
        const endDateTime   = `${yyyy}-${mm}-${dd}T${endTime}:00`;

        const payload = {
            petId, dayOfWeek, startTime: startDateTime,
            endTime: endDateTime, timeSlot, location, notes
        };

        const res = await fetch('/schedules', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(payload)
        });
        if (!res.ok) {
            const err = await res.text();
            throw new Error(err || '일정 저장 실패');
        }

        document.getElementById('scheduleModal').style.display = 'none';
        await loadSchedules();  // 기존 자기 일정 리로드 함수
        alert('일정이 저장되었습니다.');
    } catch (err) {
        console.error(err);
        alert(err.message);
    }
}

/* 일정 상세 처리 */
function openScheduleDetail(scheduleId, status) {
    const modal = document.getElementById('scheduleDetailModal');
    const scheduleDelete = document.getElementById('scheduleDeleteBtn');
    modal.style.display = 'block';

    if(status === 'user'){
        scheduleDelete.style.display = 'block';
    } else {
        scheduleDelete.style.display = 'none';
    }

    fetchScheduleData(scheduleId);
}

async function deleteSchedule() {
    if (confirm('정말로 이 일정을 삭제하시겠습니까?')) {
        try {
            const scheduleId = document.getElementById('scheduleDetailModal').dataset.scheduleId;
            const response = await fetch(`/schedules/${scheduleId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert('일정이 삭제되었습니다.');
                closeModal();
                // 페이지 새로고침 또는 일정 목록 업데이트
                window.location.reload();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.message || '일정 삭제 중 오류가 발생했습니다.');
            }
        } catch (error) {
            alert(error.message);
            console.error('일정 삭제 오류:', error);
        }
    }
}

async function fetchScheduleData(scheduleId) {
    try {
        // API 호출
        const response = await fetch(`/schedules/detail/${scheduleId}`);

        if (!response.ok) {
            throw new Error('일정 정보를 가져오는데 실패했습니다.');
        }

        // 응답 데이터 파싱
        const data = await response.json();

        // 모달에 scheduleId 저장
        document.getElementById('scheduleDetailModal').dataset.scheduleId = scheduleId;

        // 모달에 데이터 채우기
        updateModalWithData(data);

    } catch (error) {
        console.error('일정 데이터 가져오기 오류:', error);
        alert('일정 정보를 불러오는데 실패했습니다.');
    }
}

function updateModalWithData(data) {
    // 요일 변환
    const dayNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
    const dayOfWeek = dayNames[data.dayOfWeek];

    const slotMap = { AM:'오전', PM:'오후', EVENING:'저녁' };
    const timeSlot = slotMap[data.timeSlot];

    // 시간 형식 변환
    const startTime = new Date(data.startTime);
    const endTime = new Date(data.endTime);
    const formattedStartTime = startTime.toLocaleTimeString('ko-KR', {hour: '2-digit', minute:'2-digit'});
    const formattedEndTime = endTime.toLocaleTimeString('ko-KR', {hour: '2-digit', minute:'2-digit'});

    // 강아지 나이 계산
    const birthday = new Date(data.pet.birthday);
    const today = new Date();
    const ageInYears = Math.floor((today - birthday) / (365.25 * 24 * 60 * 60 * 1000));

    // 일정 정보 업데이트
    document.getElementById('detail-day').textContent = dayOfWeek;
    document.getElementById('detail-period').textContent = timeSlot;
    document.getElementById('detail-time').textContent = `${formattedStartTime} ~ ${formattedEndTime}`;
    document.getElementById('detail-place').textContent = data.location;
    document.getElementById('detail-memo').textContent = data.notes || '메모 없음';

    // 강아지 정보 업데이트
    document.getElementById('pet-name').textContent = data.pet.name;
    document.getElementById('pet-breed').textContent = data.pet.breed;

    // 생일 형식 변환
    const birthDate = new Date(data.pet.birthday);
    document.getElementById('pet-birthday').textContent = birthDate.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });

    document.getElementById('pet-age').textContent = `${ageInYears}세`;

    // 강아지 이미지
    const petImage = document.getElementById('pet-image');
    if (data.pet.imageUrl) {
        petImage.src = data.pet.imageUrl;
        petImage.alt = data.pet.name;
    } else {
        petImage.src = '/assets/image/default-dog.png'; // 기본 이미지
    }
}

function closeModal() {
    const modal = document.getElementById('scheduleDetailModal');
    modal.style.display = 'none';
}