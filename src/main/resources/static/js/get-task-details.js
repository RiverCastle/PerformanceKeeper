// 과제 데이터를 불러올 API 엔드포인트
const apiUrl = '/api/course/' + course_id + '/task/' + assigned_task_id;

// document가 로드된 후 실행되는 함수
fetch(apiUrl, {
    headers: {
        'Authorization': auth
    },
    method: "GET"
})
    .then(response => response.json())
    .then(data => {
        document.getElementById('task-name-tag').textContent = data.name;
        // 텍스트 데이터를 가져와서 HTML 요소로 변환하여 container에 추가
        const textElement = document.createElement('p');
        textElement.textContent = data.description;
        document.getElementById('task-details-container').appendChild(textElement);
    })
    .catch(error => {
        console.error('데이터를 불러오는 중 오류 발생:', error);
    });
