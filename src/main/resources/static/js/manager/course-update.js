const btn_update_course_name = document.getElementById('btn-update-course-name')
const btn_update_course_desc = document.getElementById('btn-update-course-desc')

btn_update_course_name.addEventListener('click', () => {
    const new_name_input = prompt("새로운 강의실 이름을 입력해주세요 : ", course_name_tag.textContent)
    if (new_name_input !== null) {
        fetch('/api/course/' + course_id + '/name', {
            headers: {
                'Authorization': auth,
                'Content-Type': 'application/json'
            },
            method: "PUT",
            body: JSON.stringify({
                "newName": new_name_input
            })
        })
            .then(response => {
                if (response.ok) {
                    alert("수정이 완료되었습니다.")
                    window.location.reload()
                } else response.json().then(error => {
                    alert(error.message);
                })
            })
    }
})

btn_update_course_desc.addEventListener('click', () => {
    const new_desc_input = prompt("새로운 강의실 소개를 입력해주세요 : ", course_desc_tag.textContent)
    if (new_desc_input !== null) {
        fetch('/api/course/' + course_id + '/description', {
            headers: {
                'Authorization': auth,
                'Content-Type': 'application/json'
            },
            method: "PUT",
            body: JSON.stringify({
                "newDescription": new_desc_input
            })
        })
            .then(response => {
                if (response.ok) {
                    alert("수정이 완료되었습니다.")
                    window.location.reload()
                } else response.json().then(error => {
                    alert(error.message);
                })
            })
    }
})