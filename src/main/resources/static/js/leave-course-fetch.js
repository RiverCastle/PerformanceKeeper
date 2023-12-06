const btn_leave_course = document.getElementById('course-leave-button');
btn_leave_course.addEventListener('click', () => {
    const course_name_input = prompt("이 팀을 나가시려면, 팀의 이름을 입력해주세요: ");
    fetch('/api/course/' + course_id + '/member', {
        headers: {
            'Authorization' : auth,
            'Content-Type' : 'application/json'
        },
        method: 'DELETE',
        body: JSON.stringify({
            'courseNameCheck' : course_name_input
        })
    })
        .then(response => {
            if (response.ok) window.location.href = '/views/main'
            else response.json().then(error => alert(error.message));
        })
})