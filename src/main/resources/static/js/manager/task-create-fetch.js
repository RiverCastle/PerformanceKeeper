const courseId = getCourseIdFromURL();
function create_task() {
    const name = document.getElementById("task_name_input").value;
    const description = document.getElementById("task_desc_input").value;
    const start = document.getElementById("task_start_at").value;

    fetch('/api/course/' + courseId + '/task', {
        headers: {
            "Authorization": auth,
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify({
            "name": name,
            "description": description,
            "startAt": start
        })
    })
        .then(response => {
            if (response.ok) {
                alert("성공적으로 과제가 등록되었습니다.")
            //     과제 목록 페이지로 이동
            }
        })
        .catch(error => {
            alert(error.method);
            window.location.href="/views/login";
        });
}
function getCourseIdFromURL() {
    const url = window.location.href;
    const match = url.match(/\/views\/manager\/course\/(\d+)\/task/);
    if (match) {
        return match[1];
    } else {
        throw new Error("Team ID not found in URL");
    }
}