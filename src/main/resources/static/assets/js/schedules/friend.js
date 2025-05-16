/* 친구 리스트 불러오기 */
async function loadFriends() {
    try {
        const response = await fetch('/friends');
        const data = await response.json();

        renderFriends(data.friends);
    } catch (error) {
        console.error('친구 목록 불러오기 오류:', error);
    }
}

function renderFriends(friends) {
    const container = document.querySelector('.friend-list');
    container.innerHTML = '';

    friends.forEach(f => {
        const petsText = Array.isArray(f.pets) && f.pets.length > 0
            ? f.pets.map(p => `${escapeHtml(p.name)}(${escapeHtml(p.breed)})`).join(', ')
            : '없음';

        const card = document.createElement('div');
        card.className = 'friend-card';
        card.dataset.friendId = f.friendId;

        card.innerHTML = `
                <div class="friend-header">
                  <div class="friend-avatar">
                    <i class="fas fa-user"></i>
                  </div>
                  <div class="friend-info">
                    <div class="friend-name">${escapeHtml(f.friendName)}</div>
                    <div class="friend-pets">반려동물: ${escapeHtml(petsText)}</div>
                    <div class="friend-duration">친구 기간: ${formatDuration(f.createdAt)}</div>
                  </div>
                </div>
                <div class="friend-body">
                  <div class="friend-actions">
                    <button type="button" class="btn btn-primary view-schedule-btn">시간표 보기</button>
                    <button class="btn btn-outline btn-secondary remove-friend-btn">친구 삭제</button>
                  </div>
                </div>
              `;

        const removeBtn = card.querySelector('.remove-friend-btn');
        removeBtn.addEventListener('click', () => removeFriend(f.friendId, card));

        const viewScheduleBtn = card.querySelector('.view-schedule-btn');
        viewScheduleBtn.addEventListener('click', () => loadFriendSchedules(f.friendId, card));

        container.appendChild(card);
    });
}

function loadFriendSchedules(friendId, cardElement) {
    const friendName = cardElement.querySelector('.friend-name').textContent;

    document.querySelector('.friend-list').style.display = 'none';
    document.querySelector('#friend-schedule-header').style.display = 'none';

    const friendScheduleContainer = document.querySelector('.friend-schedule-container');
    friendScheduleContainer.style.display = 'block';

    document.getElementById('selectedFriendName').textContent = friendName + '님의 산책 시간표';

    loadFriendSchedulesTable(friendId);
}

function removeFriend(friendId, cardElement) {
    if (!confirm('정말 친구를 삭제하시겠습니까?')) return;
    fetch(`/friends/${friendId}`, {
        method: 'DELETE'
    }).then(res => {
        if (!res.ok) throw new Error('삭제 실패');
        cardElement.remove();
    }).catch(err => alert(err.message));
}

/* 친구 검색 */
async function handleFriendSearch() {
    const input = document.getElementById('friendSearchInput');
    const query = input.value.trim();
    if (!query) {
        alert('이메일을 입력하세요.');
        return;
    }

    try {
        const res = await fetch(`/friends/search?email=${encodeURIComponent(query)}`);
        if (!res.ok) {
            alert('사용자를 찾을 수 없습니다.');
            clearFriendResult();
            return;
        }

        const data = await res.json(); // FriendSearchResponse
        renderFriendResult(data);
    } catch (err) {
        console.error(err);
        alert('검색 중 오류가 발생했습니다.');
    }
}

function renderFriendResult(friend) {
    const container = document.getElementById('friendSearchResults');
    container.innerHTML = `
            <h4 class="mb-10">검색 결과</h4>
            <div class="friend-card" data-friend-id="${friend.friendId}">
              <div class="friend-header">
                <div class="friend-avatar"><i class="fas fa-user"></i></div>
                <div class="friend-info">
                  <div class="friend-name">${escapeHtml(friend.friendName)}</div>
                  <div class="friend-pets">
                    반려동물: ${formatPetList(friend.pets)}
                  </div>
                </div>
              </div>
              <div class="friend-body">
                <button class="btn btn-primary" id="sendFriendRequest"
                    ${friend.friendStatus ? 'disabled' : ''}>
                    ${friend.friendStatus ? '내 친구' : '친구 맺기'}
                </button>
            </div>
            </div>
          `;

    container.style.display = 'block';

    document.getElementById('sendFriendRequest').addEventListener('click', () => {
        sendFriendRequest(friend.friendId);
    });
}

function clearFriendResult() {
    const container = document.getElementById('friendSearchResults');
    const input = document.getElementById('friendSearchInput');

    container.innerHTML = '';
    container.style.display = 'none';

    input.value = '';
}

async function sendFriendRequest(friendId) {
    try {
        const res = await fetch('/friends', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ friendId })
        });
        if (!res.ok) throw new Error('친구 맺기 실패');

        alert('시간표를 확인할 수 있습니다.');
        await loadFriends();
    } catch (err) {
        console.error(err);
        alert(err.message);
    }
}