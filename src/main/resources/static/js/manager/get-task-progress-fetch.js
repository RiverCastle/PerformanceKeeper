const btn = document.getElementById('task-search-btn');
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
            progress_table.className = 'fl-table'

            // 요구사항 1: 1열 head "유저이름"
            const thead = progress_table.createTHead();
            const headerRow = thead.insertRow(0);
            const usernameHeader = headerRow.insertCell(0);
            usernameHeader.textContent = "유저이름";

            // 요구사항 2: taskList 키에 들어 있는 List 값의 name 필드 값들을 2열부터 리스트에 담기
            const taskList = map.taskList;
            taskList.forEach((task, index) => {
                const taskNameHeader = headerRow.insertCell(index + 1);
                const task_id = task.id;
                taskNameHeader.textContent = task.name;
                taskNameHeader.addEventListener('mouseover', () => {
                    fetch('/api/course/' + course_id + '/task/' + task_id + '/progress', {
                        headers: {
                            "Authorization": auth
                        },
                        method: "GET"
                    })
                        .then(response => {
                            if (response.ok) {
                                response.json().then(dataArr => {
                                    alert(task.name + "과제 진행 상황" +
                                        "\n완료 : " + dataArr[0] +
                                        "\n진행중 : " + dataArr[1] +
                                        "\n에러 : " + dataArr[2] +
                                        "\n등록 : " + dataArr[1])
                                })
                            } else response.json().then(error => alert(error.message))
                        })
                })
            });

            // 요구사항 3: tbody 생성 및 progresses 키에 들어있는 맵 객체의 데이터 추가
            const tbody = progress_table.createTBody();
            for (const [userOverview, assignedTasks] of Object.entries(map.progresses)) {
                const row = tbody.insertRow();
                const usernameCell = row.insertCell(0);
                const nickname = userOverview.match(/nickname=(.*?)(?=\))/);
                usernameCell.textContent = nickname[1].trim();

                assignedTasks.forEach((assignedTask, index) => {
                    const taskStatusCell = row.insertCell(index + 1);
                    const taskStatusTag = document.createElement('a');
                    taskStatusTag.href = '/views/manager/course/' + course_id + '/assignedTask/' + assignedTask.id;
                    taskStatusTag.textContent = assignedTask.status;
                    taskStatusCell.appendChild(taskStatusTag);
                });
            }

            div_progress_wrapper.appendChild(progress_table);
        })
})
