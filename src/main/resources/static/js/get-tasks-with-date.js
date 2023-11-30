const uncompleted_tasks_wrapper = document.getElementById("uncompleted-tasks-wrapper");
const completed_tasks_wrapper = document.getElementById("completed-tasks-wrapper");
const courseId = getCourseId()
    function getCourseId() {
        const url = window.location.href;
        const match = url.match(/\/views\/course\/(\d+)/);
        if (match) {
            return match[1];
        } else {
            throw new Error("Team ID not found in URL");
        }
    }

const task_search_btn = document.getElementById('task-search-btn');
task_search_btn.addEventListener('click', () => {
    const date_input = document.getElementById("task-date-search-input").value;
    alert(date_input + "검색시작")
    fetch('/api/course/' + courseId + '/task?keyword=' + date_input, {
        headers: {
            'Authorization': auth
        },
        method: "GET"
    })
        .then(response => response.json())
        .then(list => {
            const uncompleted_list = list[1];
            const completed_list = list[2];

            uncompleted_list.forEach(taskDto => {
                const task_id = taskDto.id;
                const task_name = taskDto.name;
                const task_status = taskDto.status;

                const task_table = document.createElement('table');
                const task_name_cell = document.createElement('tr');
                const task_link = document.createElement('a');
                task_link.href = '/views/course/' + courseId + '/task/' + task_id;
                task_link.textContent = task_name;
                task_name_cell.appendChild(task_link);
                task_table.appendChild(task_name_cell);

                const task_status_cell = document.createElement('tr');
                // 드롭다운 버튼 생성
                var dropdownButton = document.createElement('button');
                dropdownButton.className = 'btn btn-secondary btn-sm dropdown-toggle';
                dropdownButton.type = 'button';
                dropdownButton.setAttribute('data-toggle', 'dropdown');
                dropdownButton.setAttribute('aria-expanded', 'false');
                dropdownButton.innerText = task_status;

                // 드롭다운 메뉴 생성
                var dropdownMenu = document.createElement('ul');
                dropdownMenu.className = 'dropdown-menu';

                // 메뉴 아이템들 생성
                var menuItems
                if (task_status === '완료') {
                    menuItems = ['진행중', '에러'];
                } else if (task_status === '진행중') {
                    menuItems = ['완료', '에러'];
                } else if (task_status === '에러') {
                    menuItems = ['완료', '진행중']
                } else if (task_status === '등록') {
                    menuItems = ['완료', '에러', '진행중'];
                }

                // 아이템을 버튼에 추가
                menuItems.forEach(menuItem => {
                    var menuItemLi = document.createElement('li');
                    menuItemLi.className = 'dropdown-item';
                    menuItemLi.innerText = menuItem;

                    // 클릭 이벤트 리스너 추가
                    menuItemLi.addEventListener('click', function (event) {
                        // 선택된 아이템의 텍스트 가져오기
                        var selectedStatus = event.target.innerText;

                        // 여기서 Fetch API 호출을 수행
                        // fetchApi(selectedStatus);
                        fetch('/api/course/' + courseId + '/task/' + task_id, {
                            headers: {
                                'Authorization': auth,
                                'Content-Type': 'application/json'
                            },
                            method: "PUT",
                            body: {
                                'selectedStatus': selectedStatus
                            }
                        })
                            .then(response => {
                                if (response.ok) window.location.reload();
                                else {
                                    response.json().then(error => alert(error.message))
                                }
                            })
                    })
                    dropdownMenu.appendChild(menuItemLi);
                })
                task_status_cell.appendChild(dropdownButton);
                uncompleted_tasks_wrapper.appendChild(task_table);
            })

            completed_list.forEach(taskDto => {


                completed_tasks_wrapper.appendChild(taskDto);
            })
        })
})
