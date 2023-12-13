const add_comment_btn = document.getElementById("btn-to-add-comment");
const comment_div = document.getElementById('comments-container');
add_comment_btn.addEventListener('click', () => {
    const comment_content_input = document.getElementById('comment-text-input').value;
    if (comment_content_input !== null) {
        fetch('/api/course/' + course_id + '/assignedTask/' + assigned_task_id + '/comment', {
            headers: {
                'Authorization' : auth,
                'Content-Type' : 'application/json'
            },
            method: "POST",
            body: JSON.stringify({
                'content': comment_content_input
            })
        })
            .then(response => {
                if (response.ok) window.location.reload();
                else response.json().then(error => {
                    alert(error.message)
                })
            })
    }
})