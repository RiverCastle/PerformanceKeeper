const btn = document.getElementById('task-search-btn');
const course_id = getCourseId()
btn.addEventListener('click', () => {
    const div_progress_wrapper = document.getElementById('task-progress-wrapper');
    div_progress_wrapper.innerText = '';
    const date_input = document.getElementById("task-date-search-input").value;

    fetch('/api/course/' + course_id + '/task/course-progress?date=' + date_input, {
        headers: {
            'Authorization': auth
        },
        method: "GET"
    })
        .then(response => response.json())
        .then(map => {
            const progress_table = document.createElement('table');
            progress_table.className='fl-table'

            // 요구사항 1: 1열 head는 "유저이름"
            const thead = progress_table.createTHead();
            const headerRow = thead.insertRow(0);
            const usernameHeader = headerRow.insertCell(0);
            usernameHeader.textContent = "유저이름";

            // 요구사항 2: taskList 키에 들어 있는 List 값의 name 필드 값들을 2열부터 리스트에 담기
            const taskList = map.taskList;
            taskList.forEach((task, index) => {
                const taskNameHeader = headerRow.insertCell(index + 1);
                taskNameHeader.textContent = task.name;
            });

            // 요구사항 3: tbody 생성 및 progresses 키에 들어있는 맵 객체의 데이터 추가
            const tbody = progress_table.createTBody();
            for (const [userOverview, assignedTasks] of Object.entries(map.progresses)) {
                const row = tbody.insertRow();
                const usernameCell = row.insertCell(0);
                const name = userOverview.match(/name=(.*?)(?=\))/);
                usernameCell.textContent = name[1].trim();

                assignedTasks.forEach((assignedTask, index) => {
                    const taskStatusCell = row.insertCell(index + 1);
                    taskStatusCell.textContent = assignedTask.status;
                });
            }

            div_progress_wrapper.appendChild(progress_table);
        })
})

function getCourseId() {
    const url = window.location.href;
    const match = url.match(/\/views\/manager\/course\/(\d+)/);
    if (match) {
        return match[1];
    } else {
        throw new Error("Team ID not found in URL");
    }
}