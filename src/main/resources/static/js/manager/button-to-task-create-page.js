const btn_to_task_create_page = document.getElementById('btn-to-task-create-page')
btn_to_task_create_page.addEventListener('click', () => {
    window.location.href = '/views/manager/course/' + course_id + '/task-create-page';
})