const btn_leave_course = document.getElementById('course-leave-button');
btn_leave_course.addEventListener('click', () => {
    fetch('/api/course/' + course_id + '/member', {
        headers: {
            'Authorization' : auth
        },
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) window.location.href = '/views/main'
            else response.json().then(error => alert(error.message));
        })
})