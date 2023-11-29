// 현재 날짜를 가져오는 함수
function getCurrentDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// input 요소의 값을 현재 날짜로 설정
document.getElementById('task-date-search-input').value = getCurrentDate();